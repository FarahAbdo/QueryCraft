
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SimpleDBMSGUI extends JFrame {
    private final Table table;
    private final JTextArea outputArea;
    private final JTextField inputField;

    public SimpleDBMSGUI() {
        super("Simple DBMS GUI");
        this.table = new Table();
        this.setLayout(new BorderLayout());

        // Text area for output
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        this.add(scrollPane, BorderLayout.CENTER);

        // Input field for commands
        inputField = new JTextField();
        this.add(inputField, BorderLayout.SOUTH);
        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String command = inputField.getText();
                processCommand(command);
                inputField.setText("");  // Clear input field after processing
            }
        });

        // Button to display binary file contents
        JButton showFileButton = new JButton("Show File Contents");
        showFileButton.addActionListener(e -> displayFileContents());
        this.add(showFileButton, BorderLayout.NORTH);

        this.setSize(600, 400);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    private void processCommand(String command) {
        try {
            if (command.toLowerCase().startsWith("insert")) {
                String[] parts = command.split(" ");
                int id = Integer.parseInt(parts[1]);
                String name = parts[2];
                int age = Integer.parseInt(parts[3]);
                Record record = new Record(id, name, age);
                table.insert(record);
                outputArea.append("Record inserted: " + record + "\n");
                table.saveRecords();  // Save records to file after each insert
            } else if (command.toLowerCase().startsWith("select")) {
                List<Record> records = table.select();
                if (records.isEmpty()) {
                    table.loadRecords();
                }
                records = table.select(); // refresh the local variable after potentially reloading
                if (records.isEmpty()) {
                    outputArea.append("No records found.\n");
                } else {
                    for (Record record : records) {
                        outputArea.append(record + "\n");
                    }
                }
            } else {
                outputArea.append("Unknown command.\n");
            }
        } catch (Exception e) {
            outputArea.append("Error processing command: " + e.getMessage() + "\n");
        }
    }

    private void displayFileContents() {
        List<Record> records = table.loadRecords();
        outputArea.append("Contents of binary file:\n");
        for (Record record : records) {
            outputArea.append(record.toString() + "\n");
        }
    }

    public static void main(String[] args) {
        new SimpleDBMSGUI();  // Start the GUI
    }

    static class Table {
        private final List<Record> records;
        private static final String FILENAME = "database.bin";

        public Table() {
            this.records = new ArrayList<>();
            loadRecords();  // Load records from file on start
        }

        public void insert(Record record) {
            records.add(record);
        }

        public List<Record> select() {
            return new ArrayList<>(records);
        }

        public void saveRecords() {
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILENAME))) {
                out.writeObject(records);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public List<Record> loadRecords() {
            File file = new File(FILENAME);
            if (file.exists()) {
                try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILENAME))) {
                    List<Record> loadedRecords = (List<Record>) in.readObject();
                    records.clear();  // Clear existing records before loading new ones
                    records.addAll(loadedRecords);
                    return loadedRecords;
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            return new ArrayList<>();
        }
    }

    static class Record implements Serializable {
        private final int id;
        private final String name;
        private final int age;

        public Record(int id, String name, int age) {
            this.id = id;
            this.name = name;
            this.age = age;
        }

        @Override
        public String toString() {
            return String.format("Record{id=%d, name='%s', age=%d}", id, name, age);
        }
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.sql_dbms;

/**
 *
 * @author hp
 */
public class Shell extends javax.swing.JFrame {

    /**
     * Creates new form NewJFrame
     */
    
    long time;
    int lastQuryBegin=38;
    public Shell() {
        initComponents();
        load();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("QueryCraft Shell");

        jTextArea1.setBackground(new java.awt.Color(0, 0, 0));
        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Segoe Print", 1, 18)); // NOI18N
        jTextArea1.setForeground(new java.awt.Color(255, 255, 255));
        jTextArea1.setRows(5);
        jTextArea1.setText("Hellow enter your u and p\nQueryCraft >");
        jTextArea1.setToolTipText("");
        jTextArea1.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        jTextArea1.setName(""); // NOI18N
        jTextArea1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextArea1KeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextArea1KeyTyped(evt);
            }
        });
        jScrollPane1.setViewportView(jTextArea1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 699, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 489, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents
    private String query(String queryStatment){
    queryStatment = queryStatment.replaceAll("\n                >"," ");
    queryStatment = queryStatment.replaceAll("( )+"," ").toUpperCase();
    //System.out.println(queryStatment);
    return "\n"+queryStatment;
    } 
    private void jTextArea1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextArea1KeyPressed
        String in = jTextArea1.getText();
        if(evt.getExtendedKeyCode()== 10){
           if(in.charAt(in.length()-1)== ';'){
                time = System.currentTimeMillis();
                        

              //System.out.println(in.substring(lastQuryBegin));
             jTextArea1.append( query(in.substring(lastQuryBegin)));
             
              jTextArea1.append("\nmillis is "+(System.currentTimeMillis()-time));
            jTextArea1.append("\nQueryCraft >");
            lastQuryBegin=jTextArea1.getText().length();
           }
            else if(in.substring(in.length()-12,in.length()).equals("QueryCraft >")){
            jTextArea1.append("\nQueryCraft >");
            lastQuryBegin=in.length()+13;
            }
            else if(in.substring(in.length()-3,in.length()).toLowerCase().equals("cls")){
            jTextArea1.setText("QueryCraft >");
            lastQuryBegin=12;
            }
            else if(in.substring(in.length()-4,in.length()).toLowerCase().equals("exit")){
            this.dispose();
            }
            else
            jTextArea1.append("\n                >");
            
        }else if((evt.getExtendedKeyCode()>=44 && evt.getExtendedKeyCode()<=93)||evt.getExtendedKeyCode()==32||evt.getExtendedKeyCode()==153||evt.getExtendedKeyCode()==222) {
         jTextArea1.append(evt.getKeyChar()+"");
        }
        else if(evt.getExtendedKeyCode()== 8 && !in.substring(in.length()-12,in.length()).equals("QueryCraft >")&& !in.substring(in.length()-17,in.length()).equals("                >")){
         jTextArea1.setText(in.substring(0, in.length()-1));
        }
//else {
           //  jTextArea1.append(""+evt.getExtendedKeyCode());
        //}
        
        
       jTextArea1.setCaretPosition(jTextArea1.getDocument().getLength());
    }//GEN-LAST:event_jTextArea1KeyPressed

    private void jTextArea1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextArea1KeyTyped
       // TODO add your handling code here:
    }//GEN-LAST:event_jTextArea1KeyTyped

    public void load(){
        
    this.setVisible(true);
     jTextArea1.setEditable(false);
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Shell.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Shell.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Shell.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Shell.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Shell().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    // End of variables declaration//GEN-END:variables
}

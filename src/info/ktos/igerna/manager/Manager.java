/*
 * Igerna, version 0.2
 *
 * Copyright (C) Marcin Badurowicz 2010
 *
 *
 * This file is part of Igerna.
 *
 * Igerna is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Igerna is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Igerna. If not, see <http://www.gnu.org/licenses/>.
 */

package info.ktos.igerna.manager;

import info.ktos.igerna.Config;
import info.ktos.igerna.UserCredentialsProvider;
import java.io.IOException;
import javax.swing.JOptionPane;

/**
 * Klasa reprezentująca okno okienkowej aplikacji do zarządzania
 * serwerem Igerna
 */
public class Manager extends javax.swing.JFrame
{

    private Config conf;

    /** Creates new form Manager */
    public Manager()
    {
        initComponents();

        // wczytywanie konfiguracji
        try
        {
            conf = new Config("igerna.conf");
            conf.readFile();
        }
        catch (IOException ex)
        {
            JOptionPane.showMessageDialog(rootPane, "Wystąpił błąd odczytu pliku konfiguracyjnego, tworzę nowy plik.", "Igerna", JOptionPane.WARNING_MESSAGE, null);
        }

        jtfHost.setText(conf.getStringEntry("bind", "host", "127.0.0.1"));
        jtfPort.setText(conf.getStringEntry("bind", "port", "5222"));
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jtpTabs = new javax.swing.JTabbedPane();
        jpServerMan = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jtfHost = new javax.swing.JTextField();
        jtfPort = new javax.swing.JTextField();
        jpUserMan = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jUsersTable = new javax.swing.JTable();
        jpButtons = new javax.swing.JPanel();
        jbOK = new javax.swing.JButton();
        jbCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Igerna - konfiguracja");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setName("mainFrame"); // NOI18N

        jLabel2.setText("Port na którym serwer będzie nasłuchiwał:");

        jLabel1.setText("Host lub adres IP na którym serwer będzie nasłuchiwał:");

        jtfHost.setText("127.0.0.1");

        jtfPort.setText("5222");

        javax.swing.GroupLayout jpServerManLayout = new javax.swing.GroupLayout(jpServerMan);
        jpServerMan.setLayout(jpServerManLayout);
        jpServerManLayout.setHorizontalGroup(
            jpServerManLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpServerManLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jpServerManLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpServerManLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jtfPort, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtfHost, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(118, Short.MAX_VALUE))
        );
        jpServerManLayout.setVerticalGroup(
            jpServerManLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpServerManLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jpServerManLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jtfHost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpServerManLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtfPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addContainerGap(264, Short.MAX_VALUE))
        );

        jtpTabs.addTab("Zarządzanie serwerem", jpServerMan);

        jUsersTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nazwa użytkownika", "Hasło użytkownika", "Opis użytkownika (geckos)"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jUsersTable);

        javax.swing.GroupLayout jpUserManLayout = new javax.swing.GroupLayout(jpUserMan);
        jpUserMan.setLayout(jpUserManLayout);
        jpUserManLayout.setHorizontalGroup(
            jpUserManLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpUserManLayout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(66, Short.MAX_VALUE))
        );
        jpUserManLayout.setVerticalGroup(
            jpUserManLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 329, Short.MAX_VALUE)
        );

        jtpTabs.addTab("Zarządzanie użytkownikami", jpUserMan);

        jbOK.setText("OK");
        jbOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbOKActionPerformed(evt);
            }
        });

        jbCancel.setText("Anuluj");
        jbCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbCancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpButtonsLayout = new javax.swing.GroupLayout(jpButtons);
        jpButtons.setLayout(jpButtonsLayout);
        jpButtonsLayout.setHorizontalGroup(
            jpButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpButtonsLayout.createSequentialGroup()
                .addContainerGap(397, Short.MAX_VALUE)
                .addComponent(jbOK)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbCancel)
                .addContainerGap())
        );
        jpButtonsLayout.setVerticalGroup(
            jpButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpButtonsLayout.createSequentialGroup()
                .addContainerGap(19, Short.MAX_VALUE)
                .addGroup(jpButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbOK)
                    .addComponent(jbCancel))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jtpTabs, javax.swing.GroupLayout.DEFAULT_SIZE, 523, Short.MAX_VALUE)
            .addComponent(jpButtons, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jtpTabs, javax.swing.GroupLayout.DEFAULT_SIZE, 357, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpButtons, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbCancelActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jbCancelActionPerformed
    {//GEN-HEADEREND:event_jbCancelActionPerformed

        // zamykamy aplikację, zmiany zostają zignorowane
        Manager.this.dispose();
    }//GEN-LAST:event_jbCancelActionPerformed

    private void jbOKActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jbOKActionPerformed
    {//GEN-HEADEREND:event_jbOKActionPerformed

        if (!jtfHost.getText().equals(conf.getStringEntry("bind", "host", "127.0.0.1")))
            conf.setStringEntry("bind", "host", jtfHost.getText());

        if (!jtfPort.getText().equals(conf.getStringEntry("bind", "port", "5222")))
            conf.setStringEntry("bind", "port", jtfPort.getText());

        try
        {
            conf.saveFile();
        }
        catch (IOException ex)
        {
            System.out.println(ex.getMessage());        
            JOptionPane.showMessageDialog(rootPane, "Wystąpił błąd zapisu do pliku konfiguracyjnego!", "Igerna", JOptionPane.ERROR_MESSAGE, null);
        }

        // zamykamy aplikację i zapisujemy zmiany
        Manager.this.dispose();
    }//GEN-LAST:event_jbOKActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[])
    {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Manager().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jUsersTable;
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbOK;
    private javax.swing.JPanel jpButtons;
    private javax.swing.JPanel jpServerMan;
    private javax.swing.JPanel jpUserMan;
    private javax.swing.JTextField jtfHost;
    private javax.swing.JTextField jtfPort;
    private javax.swing.JTabbedPane jtpTabs;
    // End of variables declaration//GEN-END:variables

}

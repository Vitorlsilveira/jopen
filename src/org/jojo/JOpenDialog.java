/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * JOpenDialog.java
 *
 * Created on 10.02.2011, 19:31:13
 */
package org.jojo;

import java.awt.Container;
import java.awt.Event;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.Iterator;
import javax.swing.JDialog;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.nodes.Node;
import org.openide.util.ContextAwareAction;
import org.openide.util.Exceptions;

/**
 *
 * @author jojo
 */
public class JOpenDialog extends javax.swing.JDialog {

    private JOpenDefaultListModel resultListModel = new JOpenDefaultListModel();
    private int MAX_DISPLAY_RESULTS = 40;

    /** Creates new form JOpenDialog */
    public JOpenDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        addCustomListeners();
        moveToCenterOfScreen();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jQueryField = new javax.swing.JTextField();
        jResultPane = new javax.swing.JScrollPane();
        jResultList = new javax.swing.JList();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(org.openide.util.NbBundle.getMessage(JOpenDialog.class, "JOpenDialog.title")); // NOI18N

        jQueryField.setText(org.openide.util.NbBundle.getMessage(JOpenDialog.class, "JOpenDialog.jQueryField.text")); // NOI18N
        jQueryField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jQueryFieldKeyPressed(evt);
            }
        });

        jResultList.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jResultListKeyPressed(evt);
            }
        });
        jResultPane.setViewportView(jResultList);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jResultPane, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 527, Short.MAX_VALUE)
                    .addComponent(jQueryField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 527, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jQueryField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jResultPane, javax.swing.GroupLayout.DEFAULT_SIZE, 302, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jResultListKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jResultListKeyPressed
        if (evt.getKeyCode() == 10) {
            try {
                int selectedIndex = jResultList.getSelectedIndex();
                String selectedPath = resultListModel.getPathFromElement(selectedIndex);
                FileObject fo = FileUtil.toFileObject(new File(selectedPath).getAbsoluteFile());
                DataObject newDo = DataObject.find(fo);
                Node node = newDo.getNodeDelegate();
                javax.swing.Action a = node.getPreferredAction();
                if (a instanceof ContextAwareAction) {
                    a = ((ContextAwareAction) a).createContextAwareInstance(node.getLookup());
                }
                if (a != null) {
                    a.actionPerformed(new ActionEvent(node, ActionEvent.ACTION_PERFORMED, ""));
                }

            } catch (Exception ex) {
                Exceptions.printStackTrace(ex);
            }
            this.dispose();
        }
    }//GEN-LAST:event_jResultListKeyPressed

    private void jQueryFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jQueryFieldKeyPressed
        // on cursor down
        if (evt.getKeyCode() == 40) {
            jResultList.requestFocus();
            jResultList.setSelectedIndex(0);
        }
    }//GEN-LAST:event_jQueryFieldKeyPressed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField jQueryField;
    private javax.swing.JList jResultList;
    private javax.swing.JScrollPane jResultPane;
    // End of variables declaration//GEN-END:variables

    private void addCustomListeners() {
        jQueryField.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                updateResultList();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateResultList();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });
    }

    private void updateResultList() {
        resultListModel.clear();
        String query = jQueryField.getText();
        Iterator<FileEntry> resultListIterator = SearchData.getInstance().search(query).iterator();
        int resultCount = 0;
        while (resultCount < MAX_DISPLAY_RESULTS & resultListIterator.hasNext()) {
            FileEntry fileEntry = resultListIterator.next();
            resultListModel.add(resultListModel.size(), fileEntry.getName() + " - (" + fileEntry.getPath() + ")");
            resultCount++;
        }
        jResultList.setModel(resultListModel);
    }

    public void close() {
        this.dispose();
    }

    private void moveToCenterOfScreen() {
        this.setLocationRelativeTo(null);
    }
}

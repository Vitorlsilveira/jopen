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

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Iterator;
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
    private int MAX_DISPLAY_FILENAME_LENGTH = 25;

    /** Creates new form JOpenDialog */
    public JOpenDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        addCustomListeners();
        moveToCenterOfScreen();
        setCellRenderer();
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

        jResultList.setFont(new java.awt.Font("DejaVu Sans Mono", 0, 12)); // NOI18N
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
                    .addComponent(jResultPane, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 698, Short.MAX_VALUE)
                    .addComponent(jQueryField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 698, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jQueryField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jResultPane, javax.swing.GroupLayout.DEFAULT_SIZE, 352, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jResultListKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jResultListKeyPressed
        switch (evt.getKeyCode()) {
            case KeyEvent.VK_UP:
                if (jResultList.getSelectedIndex()==0) {
                    jQueryField.requestFocus();
                }
                break;
            case KeyEvent.VK_ENTER:
                openSelectedFile();
                this.close();
                break;
            case KeyEvent.VK_ESCAPE:
                this.close();
                break;
        }
    }//GEN-LAST:event_jResultListKeyPressed

    private void jQueryFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jQueryFieldKeyPressed
        switch (evt.getKeyCode()) {
            case KeyEvent.VK_DOWN:
                jResultList.requestFocus();
                jResultList.setSelectedIndex(0);
                break;
            case KeyEvent.VK_ESCAPE:
                this.close();
                break;
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
            String listEntry = fileEntry.getName();
            if (listEntry.length() > MAX_DISPLAY_FILENAME_LENGTH) {
                listEntry = listEntry.substring(0, MAX_DISPLAY_FILENAME_LENGTH);
            }
            while (listEntry.length() < MAX_DISPLAY_FILENAME_LENGTH) {
                listEntry = listEntry.concat(" ");
            }
            String relativeFilePath = fileEntry.getPath().replace(SearchData.getInstance().getRootFolder().getAbsolutePath(), "");
            listEntry = listEntry.concat(" (").concat(relativeFilePath).concat(")");
            resultListModel.add(0, listEntry);
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

    private void openSelectedFile() {
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
    }

    private void setCellRenderer() {
        jResultList.setCellRenderer(new ResultListCellRenderer());
    }
}

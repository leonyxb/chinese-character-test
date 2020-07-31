package com.xubo.application.panel;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.io.File;
import java.util.Collections;
import java.util.Vector;
import java.util.function.Consumer;

public class FileTreePanel extends JPanel {

    Consumer<File> fileConsumer;

    JTree tree;

    /** Construct a FileTree */
    public FileTreePanel(File dir, Consumer<File> fileConsumer) {

        this.fileConsumer = fileConsumer;

        setLayout(new BorderLayout());

        // Make a tree list with all the nodes, and make it a JTree
        tree = new JTree(addNodes(null, dir));

        // Add a listener
        tree.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.getPath().getLastPathComponent();
                FileTreeNode fileTreeNode = (FileTreeNode) node.getUserObject();

                fileConsumer.accept(fileTreeNode.getFile());

            }
        });



        // Lastly, put the JTree into a JScrollPane.
        JScrollPane scrollpane = new JScrollPane();
        scrollpane.getViewport().add(tree);
        add(BorderLayout.CENTER, scrollpane);
    }

    public JTree getTree() {
        return tree;
    }

    /** Add nodes from under "dir" into curTop. Highly recursive. */
    DefaultMutableTreeNode addNodes(DefaultMutableTreeNode curTop, File dir) {
        String curPath = dir.getPath();
        DefaultMutableTreeNode curDir = new DefaultMutableTreeNode(new FileTreeNode(dir));

        if (curTop != null) { // should only be null at root
            curTop.add(curDir);
        }
        Vector ol = new Vector();
        String[] tmp = dir.list();
        for (int i = 0; i < tmp.length; i++)
            ol.addElement(tmp[i]);
        Collections.sort(ol, String.CASE_INSENSITIVE_ORDER);
        File f;
        Vector files = new Vector();
        // Make two passes, one for Dirs and one for Files. This is #1.
        for (int i = 0; i < ol.size(); i++) {
            String thisObject = (String) ol.elementAt(i);
            String newPath;
            if (curPath.equals("."))
                newPath = thisObject;
            else
                newPath = curPath + File.separator + thisObject;
            if ((f = new File(newPath)).isDirectory())
                addNodes(curDir, f);
            else
                files.addElement(thisObject);
        }
        // Pass two: for files.
        for (int fnum = 0; fnum < files.size(); fnum++) {
            FileTreeNode node = new FileTreeNode(new File(dir, (String) files.elementAt(fnum)));
            curDir.add(new DefaultMutableTreeNode(node));
        }
        return curDir;
    }

    public Dimension getMinimumSize() {
        return new Dimension(200, 400);
    }

    public Dimension getPreferredSize() {
        return new Dimension(200, 400);
    }

    /** Main: make a Frame, add a FileTree */
    public static void main(String[] av) {

        JFrame frame = new JFrame("FileTree");
        frame.setForeground(Color.black);
        frame.setBackground(Color.lightGray);
        Container cp = frame.getContentPane();

        if (av.length == 0) {
            cp.add(new FileTreePanel(new File("."), file -> System.out.println("You selected " + file.getAbsolutePath())));
        } else {
            cp.setLayout(new BoxLayout(cp, BoxLayout.X_AXIS));
            for (int i = 0; i < av.length; i++)
                cp.add(new FileTreePanel(new File(av[i]), file -> System.out.println("You selected " + file.getAbsolutePath())));
        }

        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public class FileTreeNode {
        File file;
        String display;

        public FileTreeNode(File file) {
            this.file = file;
            display = file.getName().replace(".txt", "");
        }

        @Override
        public String toString() {
            return display;
        }

        public File getFile() {
            return file;
        }

    }
}
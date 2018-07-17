import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class ButtonsPanel extends JPanel implements ItemListener {
    private JFrame f;
    private CanvasPanel c;
    private ArrayList<JCheckBox> sharpFlatCbs = new ArrayList<JCheckBox>();

    /*
        Set-up button control panel.
    */
    protected ButtonsPanel(JFrame aframe, CanvasPanel acanvas) {
        this.f = aframe;
        this.c = acanvas;

        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Panel for flat/sharp radio buttons.
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        this.add(flatSharpButtons(), gbc);

        // Panel for note checkboxes.
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        this.add(noteCheckBoxes(), gbc);
    }

    public Dimension getPreferredSize() {
        return new Dimension(1734, 50);
    }

    /*
        Sets JCheckBoxes sharpFlatCbs ArrayList text to sharps.
    */
    private void cbFlatsToSharps() {
        JCheckBox jcb;
        String sharp_note, flat_note;

        for (int i = 0; i < sharpFlatCbs.size(); i++) {
            jcb = sharpFlatCbs.get(i);
            flat_note = jcb.getText();
            sharp_note = flatToSharp(flat_note);

            jcb.setText(sharp_note);
        }
    }

    /*
        Sets JCheckBoxes sharpFlatCbs ArrayList text to sharps.
    */
    private void cbSharpsToFlats() {
        JCheckBox jcb;
        String sharp_note;
        String flat_note;

        for (int i = 0; i < this.sharpFlatCbs.size(); i++) {
            jcb = this.sharpFlatCbs.get(i);
            sharp_note = jcb.getText();
            flat_note = sharpToFlat(sharp_note);

            jcb.setText(flat_note);
        }
    }

    /*
        Returns a JPanel with the JCheckboxes created.
        Text is initially set using sharps instead of flats.
     */
    private JPanel noteCheckBoxes() {
        JPanel p = new JPanel();

        /*
            Create the checkbox. Add an ItemListener. Add it to the panel.
            JCheckBoxes whose text can have a flat or sharp
         */

        JCheckBox a_cb = new JCheckBox("A");
        a_cb.addItemListener(this);
        p.add(a_cb);

        JCheckBox a_sharp_cb = new JCheckBox("A♯");
        a_sharp_cb.addItemListener(this);
        p.add(a_sharp_cb);
        sharpFlatCbs.add(a_sharp_cb);

        JCheckBox b_cb = new JCheckBox("B");
        b_cb.addItemListener(this);
        p.add(b_cb);

        JCheckBox c_cb = new JCheckBox("C");
        c_cb.addItemListener(this);
        p.add(c_cb);

        JCheckBox c_sharp_cb = new JCheckBox("C♯");
        c_sharp_cb.addItemListener(this);
        p.add(c_sharp_cb);
        sharpFlatCbs.add(c_sharp_cb);

        JCheckBox d_cb = new JCheckBox("D");
        d_cb.addItemListener(this);
        p.add(d_cb);

        JCheckBox d_sharp_cb = new JCheckBox("D♯");
        d_sharp_cb.addItemListener(this);
        p.add(d_sharp_cb);
        sharpFlatCbs.add(d_sharp_cb);

        JCheckBox e_cb = new JCheckBox("E");
        e_cb.addItemListener(this);
        p.add(e_cb);

        JCheckBox f_cb = new JCheckBox("F");
        f_cb.addItemListener(this);
        p.add(f_cb);

        JCheckBox f_sharp_cb = new JCheckBox("F♯");
        f_sharp_cb.addItemListener(this);
        p.add(f_sharp_cb);
        sharpFlatCbs.add(f_sharp_cb);

        JCheckBox g_cb = new JCheckBox("G");
        g_cb.addItemListener(this);
        p.add(g_cb);

        JCheckBox g_sharp_cb = new JCheckBox("G♯");
        g_sharp_cb.addItemListener(this);
        p.add(g_sharp_cb);
        sharpFlatCbs.add(g_sharp_cb);

        return p;
    }

    /*
        Returns the sharp version of a flat note, otherwise "Not Found".
        Args - Flat note.
    */
    protected static String flatToSharp(String note) {
        String sharps[] = new String[] { "A♯", "C♯", "D♯", "F♯", "G♯" };
        String flats[] = new String[] { "B♭", "D♭", "E♭", "G♭", "A♭" };

        for (int i = 0; i < flats.length; i++) {
            if (flats[i].compareTo(note) == 0) {
                return sharps[i];
            }
        }

        return "Not Found";
    }

    /*
        Returns the flat version of a sharp note, otherwise "Not Found".
        Args - Sharp note.
    */
    protected static String sharpToFlat(String note) {
        String sharps[] = new String[] { "A♯", "C♯", "D♯", "F♯", "G♯" };
        String flats[] = new String[] { "B♭", "D♭", "E♭", "G♭", "A♭" };

        for (int i = 0; i < sharps.length; i++) {
            if (sharps[i].compareTo(note) == 0) {
                return flats[i];
            }
        }

        return "Not Found";
    }

    /*
        Create radio buttons to toggle between flat and sharp notes.
        Returns - Panel with the button group added.
     */
    private JPanel flatSharpButtons() {
        JPanel p = new JPanel();
        ButtonGroup b = new ButtonGroup();

        // Create buttons.
        JRadioButton sharp_rb = new JRadioButton("Sharps", true);
        JRadioButton flat_rb = new JRadioButton("Flats", false);

        // ItemListeners.
        sharp_rb.addItemListener(this);
        flat_rb.addItemListener(this);

        // Add to button group.
        b.add(sharp_rb);
        b.add(flat_rb);

        // Add to panel.
        p.add(sharp_rb);
        p.add(flat_rb);

        return p;
    }

    /*
        Work done when a checkbox or radio button is selected
        Effects: Checkbox text shows sharps or flats based on the flat/sharp radio button selection.
                 Notes are drawn as flats or sharps from the same flat/sharp radio button.
                 Only notes selected are displayed.
     */
    public void itemStateChanged(ItemEvent e) {
        // If it's note checkboxes.
        if (e.getItemSelectable() instanceof JCheckBox) {
            JCheckBox cb = (JCheckBox)e.getItem();

            // Paint only the notes that are selected.
            if (cb.isSelected()) {
                c.addUserNote(cb.getText());
                f.getContentPane().repaint();
            }
            // If the checkbox isn't selected, then remove the notes to be drawn.
            else {
                c.removeUserNote(cb.getText());
                f.getContentPane().repaint();
            }
        }
        // If it's flat/sharp radio buttons.
        if (e.getItemSelectable() instanceof JRadioButton) {
            JRadioButton jrb = (JRadioButton)e.getItem();

            // Change flat notes to sharp notes.
            if (jrb.getText().compareTo("Sharps") == 0 && jrb.isSelected()) {
                cbFlatsToSharps();
                c.setUserNotesSF("sharps");
                c.setValidNotesSF("sharps");
                f.getContentPane().repaint();
            }
            // Change sharp notes to flat notes.
            else if (jrb.getText().compareTo("Flats") == 0 && jrb.isSelected()){
                cbSharpsToFlats();
                c.setUserNotesSF("flats");
                c.setValidNotesSF("flats");
                f.getContentPane().repaint();
            }
        }
    }
}

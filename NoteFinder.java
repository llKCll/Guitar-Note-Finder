import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.lang.*;


public class NoteFinder {

    public static void main(String[] args) {
        // Create a single thread. Swing isn't thread safe.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                // Set-up frame.
                JFrame f = new JFrame("Guitar Note Finder");
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                f.setResizable(false);
                f.setLayout(new GridBagLayout());
                GridBagConstraints gbc = new GridBagConstraints();

                // Draw the fretboard with the user selected notes within a JPanel.
                CanvasPanel cp = new CanvasPanel();
                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.fill = GridBagConstraints.BOTH;
                gbc.insets = new Insets(3,3,3,3);
                f.add(cp, gbc);

                // Create buttons panel below the fretboard.
                ButtonsPanel bp = new ButtonsPanel(f, cp);
                gbc.gridx = 0;
                gbc.gridy = 1;
                gbc.fill = GridBagConstraints.BOTH;
                gbc.insets = new Insets(3,3,10,3);
                f.add(bp, gbc);

                // Pack components and make visible.
                f.pack();
                //f.setLocationRelativeTo(null);
                f.setVisible(true);
            }
        });
    }

    /*
        Returns true if valid notes are found in the user input,
        false otherwise. Only sharps are considered, not flats.
    */
    private static boolean validate(String[] user_notes) {
        String valid_notes_array[] = { "A", "A#", "B", "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#" };
        valid_notes_array = capArray(valid_notes_array);
        HashSet<String> valid_notes_set = arrayToSet(valid_notes_array);

        for (int i = 0; i < user_notes.length; i++) {
            if (!valid_notes_set.contains(user_notes[i])) {
                System.out.println(user_notes[i] + " is an invalid note.");
                return false;
            }
        }
        return true;
    }

    /*
        Returns an array with the first letter capitalized in each String.
     */
    private static String[] capArray (String[] alist) {
        String result[] = new String[alist.length];

        for (int i = 0; i < alist.length; i++) {
            String cap_str = alist[i].substring(0, 1).toUpperCase() + alist[i].substring(1);
            result[i] = cap_str;
        }
        return result;
    }

    /*
        Returns a set with contents added from an array.
     */
    private static HashSet<String> arrayToSet(String[] strings) {
        HashSet<String> valid_notes = new HashSet<String>();

        for (int i = 0; i < strings.length; i++) {
            valid_notes.add(strings[i]);
        }
        return valid_notes;
    }
}

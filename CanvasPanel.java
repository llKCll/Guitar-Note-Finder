import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.awt.geom.Line2D;
import java.awt.geom.Ellipse2D;
import java.util.Collections;
import java.util.HashSet;


public class CanvasPanel extends JPanel {
    private final int strings = 6;
    private final int frets = 24;
    private final String valid_sharp_notes[] = new String[] { "A", "A♯", "B", "C", "C♯", "D", "D♯", "E", "F", "F♯", "G", "G♯" };
    private final String valid_flat_notes[] = new String[] { "A", "B♭", "B", "C", "D♭", "D", "E♭", "E", "F", "G♭", "G", "A♭" };
    private String valid_notes_array[] = valid_sharp_notes.clone();
    private HashSet<String> user_notes = new HashSet<String>();

    public CanvasPanel() {
        //setBorder(BorderFactory.createLineBorder(Color.black));
    }

    public Dimension getPreferredSize() {
        return new Dimension(1734, 250);
    }

    // Return: true if array contains target String, false otherwise.
    private static boolean array_contains(String[] arr, String target) {
        return Arrays.asList(arr).contains(target);
    }

    // Add a note to the user note set.
    protected void addUserNote(String s) {
        user_notes.add(s);
    }

    // Remove a note from the user note set.
    protected void removeUserNote(String s) {
        user_notes.remove(s);
    }

    /*
        Change user_notes to contain only sharps or flats.
        No effect if there aren't any flat or sharp notes
        Args - "sharps" or "flats" to change to.
    */
    protected void setUserNotesSF(String sfNote) {
        String temp_note;
        HashSet<String> result = new HashSet<String>();

        // Sharps selected.
        if (sfNote.compareTo("sharps") == 0) {

            // Search for a flat note to change to sharp.
            for (String note : user_notes) {
                temp_note = ButtonsPanel.flatToSharp(note);

                // A flat note is found to change to sharp.
                if (temp_note.compareTo("Not Found") != 0) {
                    // Add the equivalent sharp note.
                    result.add(temp_note);
                }
                else {
                    // Add the non-flat/sharp note to result.
                    result.add(note);
                }
            }
            user_notes = result;
        }
        else if (sfNote.compareTo("flats") == 0) {
            // Search for a sharp note to change to flat.
            for (String note : user_notes) {
                temp_note = ButtonsPanel.sharpToFlat(note);

                // A sharp note is found to change to flat.
                if (temp_note.compareTo("Not Found") != 0) {
                    //  Add the equivalent flat note.
                    result.add(temp_note);
                }
                else {
                    // Add the non-flat/sharp note to result.
                    result.add(note);
                }
            }
            user_notes = result;
        }
        else {
            System.err.println("Error: setValidNotes invalid arg. Please use \"sharps\" or \"flats\"");
        }
    }

    /*
        Set valid_notes_array to valid_sharp_notes or valid_flat_notes.
        Args - "sharps" or "flats" to change to.
    */
    protected void setValidNotesSF(String sfNote) {
        if (sfNote.compareTo("sharps") == 0) {
            valid_notes_array = valid_sharp_notes.clone();
        }
        else if (sfNote.compareTo("flats") == 0) {

            valid_notes_array = valid_flat_notes.clone();
        }
        else {
            System.err.println("Error: setValidNotes invalid arg. Please use \"sharps\" or \"flats\"");
        }
    }

    // Draw the odd numbered and 12th fret inlay dots.
    private void draw_fretboard_inlays(Graphics2D g) {
        int x = 107;
        int y = 25;
        int i = 0;

        // Inlays on frets 1-9.
        for (i = 0; i < 5; i++) {
            g.drawString("●", x, y);
            x += 132;
        }

        // Draw 12th fret inlays.
        g.drawString("●", x+66, y-5);
        g.drawString("●", x+66, y+5);

        // Draw 15th-21st fret inlays.
        x += (132 * 2);

        for (i = 0; i < 4; i++) {
            g.drawString("●", x, y);
            x += 132;
        }

        // Draw 24th fret inlays.
        g.drawString("●", x+66, y-5);
        g.drawString("●", x+66, y+5);
    }

    // Draw the user selected notes on the fretboard.
    private void drawSelectedNote(Graphics2D g1, String note) {
        int x, y, width, height, x1, y1, currNoteIdx, currStringIdx;
        int start_notes[] = new int[strings];
        String string_notes[] = new String[] { "E", "B", "G", "D", "A", "E"};

        // Create an array of the index of the starting string note for each 6 strings.
        for (int j = 0; j < strings; j++) {
            start_notes[j] = getIndex(valid_notes_array, string_notes[j]);
        }

        currStringIdx = 0;
        y = 35;
        y1 = 55;
        width = 35;
        height = 30;

        // Loop through every fret on every string.
        for (int k = 0; k < strings; k++) {
            currNoteIdx = start_notes[currStringIdx];
            x = 28;
            x1 = 40;

            // Draw the note only if a match is found.
            for (int i = 0; i < frets; i++) {
                x += 66;
                x1 += 66;
                currNoteIdx++;

                // Reset currNote for end of array.
                if (currNoteIdx == (valid_notes_array.length)) {
                    currNoteIdx = 0;
                }

                // Draw the ellipse with the note shown within.
                if (note.compareTo(valid_notes_array[currNoteIdx]) == 0) {
                    g1.setPaint(Color.CYAN);
                    g1.fill(new Ellipse2D.Double(x, y, width, height));

                    g1.setPaint(Color.BLACK);
                    g1.setFont(new Font("default", Font.BOLD, 15));

                    if (note.length() == 1) {
                        g1.drawString(note, x1, y1);
                    }
                    else {
                        g1.drawString(note, x1-6, y1);
                    }
                }
            }
            y += 35;
            y1 += 35;
            currStringIdx++;
        }
    }

    /*
        Draw the vertical lines (frets) to the canvas.
     */
    private void drawFrets(Graphics2D g1) {
        int x1, y1, x2, y2, i;
        x1 = x2 = 75;
        y1 = 50;
        y2 = 225;

        // Draw the very left most vertical line.
        g1.draw(new Line2D.Double(x1, y1, x2, y2));

        // Draw the remaining frets.
        for (i = 0; i < frets; i++) {
            x1 += 66;
            x2 += 66;
            g1.draw(new Line2D.Double(x1, y1, x2, y2));
        }
    }

    /*
        Draw string labels to the left of the fretboard onto the canvas.
        Ex. The "E:" string for the 6th string.
     */
    private void drawStringsLabels(Graphics2D g1) {
        int x = 23;
        int y = 55;
        g1.setColor(Color.BLACK);
        g1.setFont(new Font("default", Font.BOLD, 16));
        String string_labels[ ] = { "E", "A", "D", "G", "B", "E" };

        // Start with high E string.
        Collections.reverse(Arrays.asList(string_labels));

        // Draw note letters.
        g1.drawString(string_labels[0] + ":", 23, 58);
        g1.drawString(string_labels[1] + ":", 23, 90);
        g1.drawString(string_labels[2] + ":", 23, 125);
        g1.drawString(string_labels[3] + ":", 23, 160);
        g1.drawString(string_labels[4] + ":", 23, 196);
        g1.drawString(string_labels[5] + ":", 23, 228);
    }

    /*
        Draw the horizontal lines (strings) to the canvas.
     */
    private void drawStrings(Graphics2D g1) {
        int x1, x2, y1, y2, i;
        x1 = 75;
        x2 = 1659;
        y1 = y2 = 50;

        // Draw the first high e string.
        g1.draw(new Line2D.Double(x1, y1, x2, y2));

        // Draw remaining strings.
        g1.setColor(Color.BLACK);
        for (i = 0; i < strings-1; i++) {
            y1 += 35;
            y2 += 35;
            g1.draw(new Line2D.Double(x1, y1, x2, y2));
        }
    }

    /*
        Return index of target found in a array. Else return -1 not found.
     */
    private static int getIndex(String[] arr, String target) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].compareTo(target) == 0) {
                return i;
            }
        }
        // Not found
        return -1;
    }

    /*
        Paint to the canvas.
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g1 = (Graphics2D)g;
        g1.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Line style.
        Stroke stroke = new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        g1.setStroke(stroke);

        // Draw string labels.
        drawStringsLabels(g1);

        // Draw fretboard.
        drawFrets(g1);

        // Draw guitar strings.
        drawStrings(g1);

        // Draw the user selected notes on the fretboard.
        for (String s : user_notes) {
            drawSelectedNote(g1, s);
        }

        // Draw inlays.
        draw_fretboard_inlays(g1);
    }
}

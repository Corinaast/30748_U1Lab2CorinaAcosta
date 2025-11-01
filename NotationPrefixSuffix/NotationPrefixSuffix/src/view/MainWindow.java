package view;
import controller.NotationProcessor;
import model.INotationOperations;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class MainWindow extends JFrame {
    
    private static final Color SOFT_LAVENDER = new Color(230, 230, 250);
    private static final Color PASTEL_CREAM = new Color(252, 249, 234);
    private static final Color PASTEL_PINK = new Color(255, 189, 189);
    private static final Color PASTEL_CORAL = new Color(255, 164, 164);
    private static final Color PASTEL_MINT = new Color(186, 223, 219);
    private static final Color SOFT_PEACH = new Color(255, 228, 196);
    private static final Color TEXT_SOFT = new Color(80, 80, 80);
    private static final Color BORDER_SOFT = new Color(220, 220, 230);

    public MainWindow() {
        super("Mathematical Notation Converter ");
        
        setLayout(new BorderLayout());
        getContentPane().setBackground(SOFT_LAVENDER);
        
        NotationPanel panel = new NotationPanel();
        add(panel);
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 850);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> new MainWindow());
    }

    private class NotationPanel extends JPanel implements ActionListener {
        
        private JTextField inputField;
        private JButton postfixButton;
        private JButton evaluateButton;
        private JButton prefixButton;
        private JButton clearButton; 
        private JTextArea outputArea;
        private INotationOperations controller;

        public NotationPanel() {
            this.controller = new NotationProcessor();
            
            setLayout(new BorderLayout(0, 0));
            setBackground(SOFT_LAVENDER);
            setBorder(BorderFactory.createEmptyBorder(35, 35, 35, 35));
            
            add(createHeaderAndImagePanel(), BorderLayout.NORTH);
            add(createMainCard(), BorderLayout.CENTER);
        }
        
        private JPanel createHeaderAndImagePanel() {
            JPanel topPanel = new JPanel(new BorderLayout());
            topPanel.setBackground(SOFT_LAVENDER);
            
            JPanel header = new JPanel();
            header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
            header.setBackground(SOFT_LAVENDER);
            header.setBorder(BorderFactory.createEmptyBorder(0, 0, 25, 0));
            
            JLabel titleLabel = new JLabel("Notation Calculator");
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 34));
            titleLabel.setForeground(TEXT_SOFT);
            titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            JLabel subtitleLabel = new JLabel("Conversion and evaluation with a soft style");
            subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
            subtitleLabel.setForeground(new Color(120, 120, 130));
            subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            header.add(titleLabel);
            header.add(Box.createVerticalStrut(8));
            header.add(subtitleLabel);
            
            JLabel imageLabel = new JLabel();
            try {
                URL imageUrl = new URL("https://images4.imagebam.com/9d/47/47/ME17ALMZ_o.png");
                ImageIcon originalIcon = new ImageIcon(imageUrl);
                
                int desiredSize = 90;
                Image image = originalIcon.getImage();
                Image scaledImage = image.getScaledInstance(desiredSize, desiredSize, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(scaledImage));
                imageLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
            } catch (Exception e) {
                System.err.println("Error loading image: " + e.getMessage());
                imageLabel.setText("[IMG]");
            }
            
            JPanel imagePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            imagePanel.setBackground(SOFT_LAVENDER);
            imagePanel.add(imageLabel);
            
            topPanel.add(header, BorderLayout.CENTER);
            topPanel.add(imagePanel, BorderLayout.EAST);
            
            return topPanel;
        }
        
        private JPanel createMainCard() {
            JPanel mainCard = new JPanel(new BorderLayout(0, 0));
            mainCard.setBackground(PASTEL_CREAM);
            mainCard.setBorder(BorderFactory.createCompoundBorder(
                new SoftShadowBorder(20, PASTEL_CREAM),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
            ));
            
            JPanel paddedCard = new JPanel(new BorderLayout(0, 20));
            paddedCard.setBackground(PASTEL_CREAM);
            paddedCard.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
            
            paddedCard.add(createInputSection(), BorderLayout.NORTH);
            paddedCard.add(createOutputSection(), BorderLayout.CENTER);
            
            mainCard.add(paddedCard);
            return mainCard;
        }
        
        private JPanel createInputSection() {
            JPanel section = new JPanel();
            section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
            section.setBackground(PASTEL_CREAM);
            
            JLabel inputLabel = new JLabel("INFIX EXPRESSION");
            inputLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
            inputLabel.setForeground(TEXT_SOFT);
            inputLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            inputField = new JTextField(40);
            inputField.setFont(new Font("Consolas", Font.PLAIN, 19));
            inputField.setForeground(TEXT_SOFT);
            inputField.setBackground(Color.WHITE);
            inputField.setCaretColor(PASTEL_CORAL);
            inputField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_SOFT, 2, true),
                BorderFactory.createEmptyBorder(12, 18, 12, 18)
            ));
            inputField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 55));
            inputField.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            JLabel hintLabel = new JLabel("Hint: Supports multi-digit numbers and variables (A-Z). Separate multiple expressions with a period (.). Example: 4*(5+6-(8/2^3)-7)-1. 3+A/B");
            hintLabel.setFont(new Font("Segoe UI", Font.ITALIC, 13));
            hintLabel.setForeground(new Color(180, 100, 100));
            hintLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            JPanel buttonPanel = createButtonPanel();
            buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            section.add(inputLabel);
            section.add(Box.createVerticalStrut(10));
            section.add(inputField);
            section.add(Box.createVerticalStrut(8));
            section.add(hintLabel);
            section.add(Box.createVerticalStrut(22));
            section.add(buttonPanel);
            
            return section;
        }
        
        private JPanel createButtonPanel() {
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0)); // Align to Left
            panel.setBackground(PASTEL_CREAM);
            
            postfixButton = createPastelButton("Convert to Postfix", PASTEL_MINT);
            evaluateButton = createPastelButton("Evaluate Step by Step", PASTEL_CORAL);
            prefixButton = createPastelButton("Convert to Prefix", SOFT_PEACH);
            clearButton = createPastelButton("Clear Results", SOFT_LAVENDER.darker()); // New button
            
            postfixButton.addActionListener(this);
            evaluateButton.addActionListener(this);
            prefixButton.addActionListener(this);
            clearButton.addActionListener(this); // Add listener
            
            panel.add(postfixButton);
            panel.add(evaluateButton);
            panel.add(prefixButton);
            panel.add(clearButton); // Add new button
            
            return panel;
        }
        
        private JButton createPastelButton(String text, Color baseColor) {
            JButton button = new JButton(text);
            button.setFont(new Font("Segoe UI", Font.BOLD, 15));
            button.setForeground(TEXT_SOFT);
            button.setBackground(baseColor);
            button.setFocusPainted(false);
            button.setBorderPainted(false);
            button.setContentAreaFilled(true);
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(darken(baseColor, 0.15f), 2, true),
                BorderFactory.createEmptyBorder(12, 24, 12, 24)
            ));
            button.setOpaque(true);
            
            // Mouse hover effects
            Color hoverColor = brighten(baseColor, 0.15f);
            Color pressColor = darken(baseColor, 0.1f);
            
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    button.setBackground(hoverColor);
                }
                
                @Override
                public void mouseExited(MouseEvent e) {
                    button.setBackground(baseColor);
                }
                
                @Override
                public void mousePressed(MouseEvent e) {
                    button.setBackground(pressColor);
                }
                
                @Override
                public void mouseReleased(MouseEvent e) {
                    button.setBackground(hoverColor);
                }
            });
            
            return button;
        }
        
        private JPanel createOutputSection() {
            JPanel section = new JPanel(new BorderLayout(0, 10));
            section.setBackground(PASTEL_CREAM);
            
            JLabel outputLabel = new JLabel("RESULTS AND PROCESS TRACE");
            outputLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
            outputLabel.setForeground(TEXT_SOFT);
            
            outputArea = new JTextArea(24, 60);
            outputArea.setEditable(false);
            outputArea.setBackground(PASTEL_PINK);
            outputArea.setForeground(TEXT_SOFT);
            outputArea.setFont(new Font("Consolas", Font.PLAIN, 15));
            outputArea.setLineWrap(true);
            outputArea.setWrapStyleWord(true);
            outputArea.setMargin(new Insets(18, 18, 18, 18));
            outputArea.setText("WAITING FOR INPUT...\n\nEnter a complex mathematical expression.\n\nValid Examples:\n  4*(5+6-(8/2^3)-7)-1\n  3+4*2/(1-5)^2^3\n  A*(B+C)/D");
            
            JScrollPane scrollPane = new JScrollPane(outputArea);
            scrollPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(darken(PASTEL_PINK, 0.2f), 2, true),
                BorderFactory.createEmptyBorder(2, 2, 2, 2)
            ));
            scrollPane.getVerticalScrollBar().setUnitIncrement(16);
            
            section.add(outputLabel, BorderLayout.NORTH);
            section.add(scrollPane, BorderLayout.CENTER);
            
            return section;
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            String fullInput = inputField.getText().trim();
            
            if (e.getSource() == clearButton) {
                outputArea.setText("RESULTS CLEARED.\n\nReady for new input.");
                inputField.setText("");
                return;
            }

            if (fullInput.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Please enter an infix expression.", 
                    "Empty Field", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            outputArea.setText(""); 

            try {
                if (e.getSource() == postfixButton) {
                    String infixExpression = fullInput.split("\\.")[0].trim();
                    if (infixExpression.isEmpty()) throw new IllegalArgumentException("Invalid expression.");

                    String result = controller.convertToPostfix(infixExpression);
                    outputArea.append("════════════════════════════════════════\n");
                    outputArea.append("    CONVERSION TO POSTFIX NOTATION\n");
                    outputArea.append("════════════════════════════════════════\n\n");
                    outputArea.append(result);
                    
                } else if (e.getSource() == evaluateButton) {
                    String[] expressions = fullInput.split("\\.");
                    int expCount = 0;
                    
                    outputArea.append("════════════════════════════════════════\n");
                    outputArea.append("     EXPRESSION EVALUATION\n");
                    outputArea.append("════════════════════════════════════════\n\n");
                    
                    for (String infixExpression : expressions) {
                        String cleanInfix = infixExpression.trim();
                        if (cleanInfix.isEmpty()) continue;
                        
                        expCount++;
                        outputArea.append("\n========================================================\n");
                        outputArea.append(">>> EVALUATING EXPRESSION #" + expCount + ": " + cleanInfix + "\n");
                        outputArea.append("========================================================\n\n");
                        
                        String postfijaTrace = controller.convertToPostfix(cleanInfix);
                        
                        final String POSTFIX_IDENTIFIER = "FINAL POSTFIX RESULT: ";
                        int startIndex = postfijaTrace.lastIndexOf(POSTFIX_IDENTIFIER);
                        if (startIndex == -1) {
                            throw new IllegalArgumentException("Communication Error: Postfix identifier not found in controller output.");
                        }
                        startIndex += POSTFIX_IDENTIFIER.length();
                        int endIndex = postfijaTrace.indexOf("\n", startIndex);
                        if (endIndex == -1) { 
                            endIndex = postfijaTrace.length();
                        }
                        String postfijaClean = postfijaTrace.substring(startIndex, endIndex).trim();

                        outputArea.append("--- POSTFIX CONVERSION ---\n");
                        outputArea.append(postfijaTrace);
                        outputArea.append("\n--- VARIABLE DETECTION ---\n");
                        
                        Set<Character> variables = new HashSet<>();
                        for (char c : postfijaClean.toCharArray()) {
                            if (Character.isLetter(c)) {
                                variables.add(c);
                            }
                        }
                        
                        HashMap<Character, Double> variableValues = new HashMap<>();
                        
                        if (!variables.isEmpty()) {
                            outputArea.append("Variables detected: " + variables.toString() + "\n\n");
                            for (char var : variables) {
                                String input = JOptionPane.showInputDialog(this, 
                                    "Evaluating Expression #" + expCount + ".\nVariable '" + var + "' detected.\nPlease enter its numeric value:", 
                                    "Assign Value to " + var, 
                                    JOptionPane.QUESTION_MESSAGE);
                                
                                if (input == null) {
                                    outputArea.append(">>> Evaluation canceled by user.\n");
                                    return;
                                }
                                
                                try {
                                    double value = Double.parseDouble(input.trim());
                                    variableValues.put(var, value);
                                    outputArea.append(">>> Variable '" + var + "' mapped to: " + value + "\n");
                                } catch (NumberFormatException nfe) {
                                    throw new IllegalArgumentException("The value entered for '" + var + "' must be a valid number.");
                                }
                            }
                        } else {
                            outputArea.append("No variables detected. Evaluating numeric expression...\n");
                        }
                        
                        outputArea.append("\n>>> Postfix Expression to evaluate: " + postfijaClean + "\n\n");
                        outputArea.append("--- BEGIN EVALUATION ---\n");
                        
                        String evaluationResult = controller.evaluatePostfix(postfijaClean, variableValues);
                        outputArea.append(evaluationResult);
                    }
                    
                    if (expCount == 0) {
                        outputArea.append("No valid expression found to evaluate.");
                    }

                } else if (e.getSource() == prefixButton) {
                    String infixExpression = fullInput.split("\\.")[0].trim();
                    if (infixExpression.isEmpty()) throw new IllegalArgumentException("Invalid expression.");

                    String result = controller.convertToPrefix(infixExpression);
                    outputArea.append("════════════════════════════════════════\n");
                    outputArea.append("    CONVERSION TO PREFIX NOTATION\n");
                    outputArea.append("════════════════════════════════════════\n\n");
                    outputArea.append("Infix Expression:  " + infixExpression + "\n");
                    outputArea.append("Prefix Result: " + result + "\n");
                }
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, 
                    "ERROR: " + ex.getMessage(), 
                    "Process Error", 
                    JOptionPane.ERROR_MESSAGE);
                outputArea.append("\n\n════════════════════════════════════════\n");
                outputArea.append("PROCESS ERROR:\n");
                outputArea.append(ex.getMessage());
                outputArea.append("\n════════════════════════════════════════\n");
            }
        }
        
        private Color brighten(Color color, float factor) {
            int r = Math.min(255, (int)(color.getRed() * (1 + factor)));
            int g = Math.min(255, (int)(color.getGreen() * (1 + factor)));
            int b = Math.min(255, (int)(color.getBlue() * (1 + factor)));
            return new Color(r, g, b);
        }
        
        private Color darken(Color color, float factor) {
            int r = Math.max(0, (int)(color.getRed() * (1 - factor)));
            int g = Math.max(0, (int)(color.getGreen() * (1 - factor)));
            int b = Math.max(0, (int)(color.getBlue() * (1 - factor)));
            return new Color(r, g, b);
        }
    }
    
    static class SoftShadowBorder extends AbstractBorder {
        private int radius;
        private Color color;

        SoftShadowBorder(int radius, Color color) {
            this.radius = radius;
            this.color = color;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            g2d.setColor(new Color(200, 200, 210, 80));
            g2d.fillRoundRect(x + 4, y + 4, width - 1, height - 1, radius, radius);
            
            g2d.setColor(new Color(220, 220, 230));
            g2d.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2d.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(radius/2, radius/2, radius/2, radius/2);
        }
    }
}
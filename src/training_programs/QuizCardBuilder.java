import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
import java.io.FileReader;

public class QuizCardBuilder extends JFrame {
  JPanel panel;
  Label labelQuestion;
  Label labelAnswer;
  JTextArea textQuestion;
  JTextArea textAnswer;
  JButton buttonNext;
  JButton buttonAdd;
  ArrayList<QuizAnswer> listQA = new ArrayList<QuizAnswer>();
  int thisQA = 0;

  public static void main(String[] args) {
    new QuizCardBuilder();
  }

  QuizCardBuilder() {
    super("QuizCardBuilder");
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setBounds(50, 50, 300, 300);
    BorderLayout layout = new BorderLayout();
    panel = new JPanel(layout);
    panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

    textQuestion = new JTextArea(6, 20);
    textQuestion.setLineWrap(true);
    JScrollPane scrollQuestion = new JScrollPane(textQuestion);
    scrollQuestion.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    scrollQuestion.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

    textAnswer = new JTextArea(2, 20);
    textAnswer.setLineWrap(true);
    JScrollPane scrollAnswer = new JScrollPane(textAnswer);
    scrollAnswer.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    scrollAnswer.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

    labelQuestion = new Label("Question");
    labelAnswer = new Label("Answer");

    buttonNext = new JButton("Next card");
    buttonNext.addActionListener(new NextActionListener());

    buttonAdd = new JButton("Add new card");
    buttonAdd.addActionListener(new AddActionListener());

    Box Box1 = new Box(BoxLayout.Y_AXIS);
    Box1.add(labelQuestion);
    Box1.add(scrollQuestion);
    Box1.add(labelAnswer);
    Box1.add(scrollAnswer);
    Box Box2 = new Box(BoxLayout.X_AXIS);
    Box2.add(buttonNext);
    Box2.add(buttonAdd);
    panel.add(BorderLayout.CENTER, Box1);
    panel.add(BorderLayout.SOUTH, Box2);

    JMenuBar menuBar = new JMenuBar();
    JMenu fileMenu = new JMenu("File");
    JMenuItem newMenuItem = new JMenuItem("New list");
    newMenuItem.addActionListener(new NewMenuListener());
    JMenuItem openMenuItem = new JMenuItem("Open list");
    openMenuItem.addActionListener(new OpenMenuListener());
    JMenuItem saveMenuItem = new JMenuItem("Save list");
    saveMenuItem.addActionListener(new SaveMenuListener());
    fileMenu.add(newMenuItem);
    fileMenu.add(openMenuItem);
    fileMenu.add(saveMenuItem);
    menuBar.add(fileMenu);
    this.setJMenuBar(menuBar);

    listQA.add(new QuizAnswer("Привет, как дела?", "Вася"));
    listQA.add(new QuizAnswer("Где был?", "Петя"));
    listQA.add(new QuizAnswer("Как сам?", "Кузя"));
    textQuestion.setText("");
    textAnswer.setText("");

    this.getContentPane().add(panel);
    this.pack();
    this.setVisible(true);
  }

  private class AddActionListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      if(!textQuestion.getText().equals("") && !textAnswer.getText().equals("")) {
        listQA.add(new QuizAnswer(textQuestion.getText(), textAnswer.getText()));
        thisQA++;
        textQuestion.setText("");
        textAnswer.setText("");
        for(QuizAnswer q : listQA)
          System.out.println("add   " + q.getQuestion());
      }
    }
  }

  private class NextActionListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      if(listQA.size() > 0) {
        thisQA++;
        if(thisQA >= listQA.size())
          thisQA = 0;
        textQuestion.setText(listQA.get(thisQA).getQuestion());
        textAnswer.setText(listQA.get(thisQA).getAnswer());
        for(QuizAnswer q : listQA)
          System.out.println("next  " + q.getQuestion());
      }
    }
  }

  private class NewMenuListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      if(listQA.size() > 0)
        listQA.clear();

      thisQA = 0;
      textQuestion.setText("");
      textAnswer.setText("");
    }
  }

  private class OpenMenuListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      JFileChooser fileOpen = new JFileChooser();
      int ret = fileOpen.showOpenDialog(null);
      if(ret == JFileChooser.APPROVE_OPTION)
        openList(fileOpen.getSelectedFile());
    }
  }

  private class SaveMenuListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      String s = createSaveFile();
      saveList(s);
    }
  }

  private String createSaveFile() {
    try {
    JFileChooser fileSave = new JFileChooser();
    int ret = fileSave.showSaveDialog(null);
    if(ret == JFileChooser.APPROVE_OPTION) {
      File file = fileSave.getSelectedFile();
        return file.getName();
    }
  } catch(Exception ex) {
    System.out.println("___________________2");
    ex.printStackTrace();
  }
  return null;
  }

  private void saveList(String file) {
    if(listQA.size() > 0 && !file.equals(""))
    {
      try {
        ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(file + ".qcb"));
        for(QuizAnswer q : listQA) {
          os.writeObject(q);
        }
        os.close();
      } catch(Exception e) {
        e.printStackTrace();
      }
    }
  }

  private void openList(File file) {
    listQA.clear();
      try {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));

        while(true) {
          QuizAnswer t = null;
          t = (QuizAnswer) in.readObject();
          if(t != null)
          listQA.add((QuizAnswer) t);
        }
      } catch(EOFException e) { }
      catch(Exception e) { e.printStackTrace(); }
  }

  private class QuizAnswer implements Serializable {
    private transient static final long serialVersionUID = 11111L;
    private String question;
    private String answer;

    QuizAnswer(String question, String answer) {
      this.question = question;
      this.answer = answer;
    }

    public String getQuestion() {
      return question;
    }

    public String getAnswer() {
      return answer;
    }

    public void setAnswer(String answer) {
      this.answer = answer;
    }

    public void setQuestion(String question) {
      this.question = question;
    }
  }
}

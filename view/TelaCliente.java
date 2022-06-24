package view;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import java.awt.event.WindowAdapter;
import java.util.List;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class TelaCliente extends JFrame implements ActionListener {
    private boolean readyToPlay = false;

    private JPanel contentPane;
    private JTextField txtNome;
    private JTextField txtCidade;
    private JTextField txtFruta;
    private JTextField txtFilme;
    private JTextField txtSerie;
    private JTextField txtPais;

    private JTextArea area;
    private JTextArea letra;
    private JTextArea timer;
    private JTextArea ranking;
    private JButton btnSalvar;

    public TelaCliente() {
        setTitle("Stop");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 840, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        area = new JTextArea("Clique em \"ready\" para jogar. Esperando outros hosts confirmarem...");
        area.setFont(new Font("Serif", Font.ITALIC, 12));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setEditable(false);
        area.setBounds(10, 10, 650, 160);
        area.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createCompoundBorder(
                                BorderFactory.createTitledBorder("Caixa de Texto"),
                                BorderFactory.createEmptyBorder(5, 5, 5, 5)),
                        area.getBorder()));

        contentPane.add(area);

        letra = new JTextArea("");
        letra.setFont(new Font("Franklin Gothic Book", Font.PLAIN, 34));
        letra.setLineWrap(true);
        letra.setWrapStyleWord(true);
        letra.setEditable(false);
        letra.setBounds(675, 10, 70, 70);
        letra.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createCompoundBorder(
                                BorderFactory.createTitledBorder("Letra"),
                                BorderFactory.createEmptyBorder(5, 5, 5, 5)),
                        letra.getBorder()));

        contentPane.add(letra);

        timer = new JTextArea("60");
        timer.setFont(new Font("Franklin Gothic Book", Font.PLAIN, 34));
        timer.setLineWrap(true);
        timer.setWrapStyleWord(true);
        timer.setEditable(false);
        timer.setBounds(755, 10, 70, 70);
        timer.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createCompoundBorder(
                                BorderFactory.createTitledBorder("Timer"),
                                BorderFactory.createEmptyBorder(5, 5, 5, 5)),
                        timer.getBorder()));

        contentPane.add(timer);

        ranking = new JTextArea("");
        ranking.setFont(new Font("Serif", Font.ITALIC, 12));
        ranking.setLineWrap(true);
        ranking.setWrapStyleWord(true);

        JScrollPane scrooll = new JScrollPane(ranking);

        scrooll.setBounds(675, 90, 150, 80);
        // Borda com titulo
        scrooll.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createCompoundBorder(
                                BorderFactory.createTitledBorder("Ranking"),
                                BorderFactory.createEmptyBorder(5, 5, 5, 5)),
                        scrooll.getBorder()));

        // Label
        contentPane.add(scrooll);

        JLabel lblNome = new JLabel("Nome");
        lblNome.setFont(new Font("Franklin Gothic Book", Font.BOLD, 12));
        lblNome.setBounds(10, 180, 109, 14);
        contentPane.add(lblNome);
        // Input
        txtNome = new JTextField();
        txtNome.setFont(new Font("Franklin Gothic Book", Font.BOLD, 12));
        txtNome.setBounds(10, 205, 100, 20);
        contentPane.add(txtNome);
        txtNome.setColumns(10);

        JLabel lblCidade = new JLabel("Cidade");
        lblCidade.setFont(new Font("Franklin Gothic Book", Font.BOLD, 12));
        lblCidade.setBounds(125, 180, 109, 14);
        contentPane.add(lblCidade);

        txtCidade = new JTextField();
        txtCidade.setFont(new Font("Franklin Gothic Book", Font.BOLD, 12));
        txtCidade.setBounds(125, 205, 100, 20);
        contentPane.add(txtCidade);
        txtCidade.setColumns(10);

        JLabel lblfruta = new JLabel("Fruta");
        lblfruta.setFont(new Font("Franklin Gothic Book", Font.BOLD, 12));
        lblfruta.setBounds(235, 180, 109, 14);
        contentPane.add(lblfruta);

        txtFruta = new JTextField();
        txtFruta.setFont(new Font("Franklin Gothic Book", Font.BOLD, 12));
        txtFruta.setBounds(235, 205, 100, 20);
        contentPane.add(txtFruta);
        txtFruta.setColumns(10);

        JLabel lblFilme = new JLabel("Filme");
        lblFilme.setFont(new Font("Franklin Gothic Book", Font.BOLD, 12));
        lblFilme.setBounds(345, 180, 109, 14);
        contentPane.add(lblFilme);

        txtFilme = new JTextField();
        txtFilme.setFont(new Font("Franklin Gothic Book", Font.BOLD, 12));
        txtFilme.setBounds(345, 205, 100, 20);
        contentPane.add(txtFilme);
        txtFilme.setColumns(10);

        JLabel lblSerie = new JLabel("Serie");
        lblSerie.setFont(new Font("Franklin Gothic Book", Font.BOLD, 12));
        lblSerie.setBounds(455, 180, 109, 14);
        contentPane.add(lblSerie);

        txtSerie = new JTextField();
        txtSerie.setFont(new Font("Franklin Gothic Book", Font.BOLD, 12));
        txtSerie.setBounds(455, 205, 100, 20);
        contentPane.add(txtSerie);
        txtSerie.setColumns(10);

        JLabel lblPais = new JLabel("Pais");
        lblPais.setFont(new Font("Franklin Gothic Book", Font.BOLD, 12));
        lblPais.setBounds(565, 180, 109, 14);
        contentPane.add(lblPais);

        txtPais = new JTextField();
        txtPais.setFont(new Font("Franklin Gothic Book", Font.BOLD, 12));
        txtPais.setBounds(565, 205, 100, 20);
        contentPane.add(txtPais);
        txtPais.setColumns(10);

        btnSalvar = new JButton("Ready");
        btnSalvar.setBounds(675, 203, 100, 23);
        btnSalvar.addActionListener(this);
        btnSalvar.setActionCommand("salvar");
        contentPane.add(btnSalvar);

    }

    public String getAnswers() {
        String r = "nome=" + txtNome.getText() + "&cidade=" + txtCidade.getText() + "&fruta=" +
                    txtFruta.getText() + "&filme=" + txtFilme.getText() + "&serie=" +
                    txtSerie.getText() + "&pais=" + txtPais.getText(); 
        return r;
    }
    
    public void setCounter(Integer val) {
        timer.setText(val.toString());
    }

    public int getCounter() {
        return Integer.valueOf(timer.getText());
    }

    public void clearAnswers() {
        txtNome.setText("");
        txtCidade.setText("");
        txtFruta.setText("");
        txtFilme.setText("");
        txtSerie.setText("");
        txtPais.setText("");
    }

    public void setLetter(String letter) {
        letra.setText(letter);
    }

    public void setReadyToPlay(boolean val) {
        readyToPlay = val;
    }

    public boolean getReadyToPlay() {
        return readyToPlay;
    }
    
    public void setLog(String text) {
        area.setText(text);
    }

    public void setRanking(String text) {
        ranking.setText(text);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals(this.btnSalvar.getActionCommand())) {
            readyToPlay = true;
            // TODO
        }
    }
}

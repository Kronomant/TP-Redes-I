package view;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.WindowAdapter;
import java.util.List;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class TelaCliente extends JFrame implements ActionListener {

    private JPanel contentPane;
    private JTextField txtNome;
    private JTextField txtEndereco;

    private JTextField txtFruta;
    private String txtID;
    private JComboBox cbPesquisar;
    private ButtonGroup bt = new ButtonGroup();

    private JButton btnSalvar;
    private JButton btnExcluir;
    private JButton btnEditar;
    private JButton btnPesquisar;
    private JButton btnLimpar;

    public TelaCliente() {
        setTitle("Stop");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 500, 200);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // Labels

        JLabel lblNome = new JLabel("Nome");
        lblNome.setFont(new Font("Franklin Gothic Book", Font.BOLD, 12));
        lblNome.setBounds(10, 79, 109, 14);
        contentPane.add(lblNome);
        // Input
        txtNome = new JTextField();
        txtNome.setFont(new Font("Franklin Gothic Book", Font.BOLD, 12));
        txtNome.setBounds(10, 105, 100, 20);
        contentPane.add(txtNome);
        txtNome.setColumns(10);

        JLabel lblEndereo = new JLabel("Cidade");
        lblEndereo.setFont(new Font("Franklin Gothic Book", Font.BOLD, 12));
        lblEndereo.setBounds(125, 79, 109, 14);
        contentPane.add(lblEndereo);

        txtEndereco = new JTextField();
        txtEndereco.setFont(new Font("Franklin Gothic Book", Font.BOLD, 12));
        txtEndereco.setBounds(125, 105, 100, 20);
        contentPane.add(txtEndereco);
        txtEndereco.setColumns(10);

        JLabel lblfruta = new JLabel("Fruta");
        lblfruta.setFont(new Font("Franklin Gothic Book", Font.BOLD, 12));
        lblfruta.setBounds(235, 79, 109, 14);
        contentPane.add(lblfruta);

        txtFruta = new JTextField();
        txtFruta.setFont(new Font("Franklin Gothic Book", Font.BOLD, 12));
        txtFruta.setBounds(235, 105, 100, 20);
        contentPane.add(txtFruta);
        txtFruta.setColumns(10);

        btnSalvar = new JButton("Enviar");
        btnSalvar.setBounds(350, 128, 100, 23);
        btnSalvar.addActionListener(this);
        btnSalvar.setActionCommand("salvar");
        contentPane.add(btnSalvar);

        // btnEditar = new JButton("Editar");
        // btnEditar.setBounds(280, 327, 75, 23);
        // btnEditar.setText("Editar");
        // btnEditar.addActionListener(this);
        // btnEditar.setActionCommand("editar");
        // contentPane.add(btnEditar);

        // btnLimpar = new JButton("Limpar");
        // btnLimpar.setBounds(360, 327, 75, 23);
        // btnLimpar.setText("Limpar");
        // btnLimpar.addActionListener(this);
        // btnLimpar.setActionCommand("limpar");
        // contentPane.add(btnLimpar);

        // btnExcluir = new JButton("");
        // btnExcluir.setBounds(440, 327, 75, 23);
        // btnExcluir.setText("Excluir");
        // btnExcluir.addActionListener(this);
        // btnExcluir.setActionCommand("excluir");
        // contentPane.add(btnExcluir);

        // this.carregaLista();
    }
    // public Contato montaContato(){
    // //Pega os dados digitados nos campos do formulário e atribui ao objeto da
    // classe Contato;
    // Contato c = new Contato();
    // c.setNome(this.txtNome.getText());
    // c.setEndereco(this.txtEndereco.getText());
    // c.setTelefone(this.txtTel.getText());
    // return c;
    // }
    // public Contato editaContato(int i){
    // //Pega os dados digitados nos campos do formulário e atribui ao objeto da
    // classe Contato;
    // Contato c = new Contato();
    // c.setId(i);
    // c.setNome(this.txtNome.getText());
    // c.setEndereco(this.txtEndereco.getText());
    // c.setTelefone(this.txtTel.getText());
    // return c;
    // }
    // public void carregaContatonaTela(Contato c2){
    // //Pega os dados digitados nos campos do formulário e atribui ao objeto da
    // classe Contato;
    // this.txtNome.setText(c2.getNome());
    // this.txtEndereco.setText(c2.getEndereco());
    // this.txtTel.setText(c2.getTelefone());

    // }

    // public void limpaTela(){
    // for(int i = 0; i < contentPane.getComponentCount(); i++){
    // //laço de repetição percorrendo o contentPane - JPanel, o painel principal do
    // form
    // Component c = contentPane.getComponent(i);
    // //Cria um objeto Component c que recebe o componente na posição i do laço for
    // if(c instanceof JTextField){ //se o componente c for uma instância de
    // JTextField
    // JTextField campo = (JTextField) c;
    // //cria uma variável JTextField recebendo o componente c com um cast
    // campo.setText(null);
    // //apaga o conteúdo do campo JTextField;
    // }
    // }

    // }
    // public void carregaLista(){
    // //Preenche Combobox com registros do banco de dados
    // controle.ContatoCT mbc = new ContatoCT();

    // List<Contato> ContatoBd = mbc.getContatos();
    // cbPesquisar.removeAllItems();
    // for (Contato contato : ContatoBd) {
    // cbPesquisar.addItem(contato.getNome());
    // }
    // }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals(this.btnSalvar.getActionCommand())) {
            JOptionPane.showMessageDialog(null, "Contato " + txtNome.getText() + " cadastrado...");

        } else if (e.getActionCommand().equals(this.btnPesquisar.getActionCommand())) {

        } else if (e.getActionCommand().equals(this.btnLimpar.getActionCommand())) {

        } else if (e.getActionCommand().equals(this.btnExcluir.getActionCommand())) {

        }
        if (e.getActionCommand().equals(this.btnEditar.getActionCommand())) {

        }

    }

}
    package com.sistema.mvsistema.view;

    import atlantafx.base.controls.PasswordTextField;
    import atlantafx.base.theme.Styles;
    import com.sistema.mvsistema.entity.Usuario;
    import com.sistema.mvsistema.service.UsuarioService;
    import com.sistema.mvsistema.util.CriptografiaUtil;
    import com.sistema.mvsistema.util.Utils;
    import com.sistema.mvsistema.util.Versao;
    import com.sistema.mvsistema.view.components.JanelaGeracaoVariacaoProduto;
    import javafx.geometry.Insets;
    import javafx.geometry.Pos;
    import javafx.scene.Cursor;
    import javafx.scene.Scene;
    import javafx.scene.control.*;
    import javafx.scene.image.Image;
    import javafx.scene.image.ImageView;
    import javafx.scene.layout.*;
    import javafx.scene.text.Text;
    import javafx.stage.Stage;
    import org.kordamp.ikonli.feather.Feather;
    import org.kordamp.ikonli.javafx.FontIcon;
    import org.kordamp.ikonli.material2.Material2AL;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Component;

    import java.time.LocalDateTime;
    import java.util.Objects;

    @Component
    public class TelaLogin {

        @Autowired
        private UsuarioService usuarioService;

        @Autowired
        private JanelaGeracaoVariacaoProduto janelaGeracaoVariacaoProduto;

        public Scene createScene(Runnable onLoginSuccess, Stage stage) {

            ImageView logoImageView = new ImageView();
            try {

                Image logoImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/static/imagens/tituloLogin.png")));

                logoImageView.setImage(logoImage);
                logoImageView.setFitWidth(250);
                logoImageView.setPreserveRatio(true);

            } catch (Exception e) {
                System.err.println("Erro ao carregar a imagem do logo: " + e.getMessage());
            }

            Text title1Text = new Text("Login");

            title1Text.getStyleClass().addAll(Styles.TITLE_1);

            Text usuarioText = new Text("Usuario");

            TextField usuario= new TextField();
            usuario.setPromptText("Usuario");

            VBox usuarioBox = new VBox(5, usuarioText, usuario);

            Text senhaText = new Text("Senha");


            PasswordTextField senha = new PasswordTextField("");
            senha.setPrefWidth(250);
            senha.setPromptText("Senha");
            FontIcon icon = new FontIcon(Feather.EYE_OFF);
            icon.setCursor(Cursor.HAND);

            icon.setOnMouseClicked(e -> {
                icon.setIconCode(senha.getRevealPassword()
                        ? Feather.EYE_OFF : Feather.EYE
                );
                senha.setRevealPassword(!senha.getRevealPassword());
            });
            senha.setRight(icon);

            VBox senhaBox = new VBox(5, senhaText, senha);

            Button btnLoginSuccess = new Button(
                    "Entrar", new FontIcon(Material2AL.ARROW_FORWARD)
            );
            btnLoginSuccess.getStyleClass().addAll(
                    Styles.BUTTON_OUTLINED, Styles.ACCENT
            );
            btnLoginSuccess.setContentDisplay(ContentDisplay.RIGHT);

            Button dev = new Button("Dev");

            dev.setOnAction(e ->{
                //Usuario usuario1 = new Usuario("marcos", CriptografiaUtil.gerarHash("1234"), "ADMIN", LocalDateTime.now(), true );
                //System.out.println(usuario1);
                //usuarioService.salvar(usuario1);
                //GeradorVariacoesView geradorVariacoesView= new GeradorVariacoesView();
                janelaGeracaoVariacaoProduto.mostrarJanela();
            });

            Button btnLeave = new Button("Sair");

            btnLeave.getStyleClass().addAll(
                    Styles.BUTTON_OUTLINED, Styles.DANGER
            );

            btnLeave.setContentDisplay(ContentDisplay.RIGHT);
            btnLoginSuccess.setDefaultButton(true);

            btnLeave.setPrefWidth(100);

            Region spacerFooter = new Region();
            HBox.setHgrow(spacerFooter, Priority.ALWAYS);
            HBox btnLogin = new HBox(5,dev, spacerFooter, btnLeave, btnLoginSuccess);
            btnLogin.setAlignment(Pos.CENTER_RIGHT);

            usuario.setText("marcos");
            senha.setText("1234");


            btnLeave.setOnAction(e -> {
                stage.close();
            });

            Label versao = new Label("VERSÃO: " + Versao.getVersaoApp());
            versao.setStyle("-fx-text-fill: black; -fx-font-size: 11px; -fx-font-weight: bold;");
            versao.setAlignment(Pos.CENTER);
            versao.setMaxWidth(Double.MAX_VALUE);

            // Layout
            VBox layoutCentro = new VBox(15, logoImageView, title1Text, usuarioBox, senhaBox, btnLogin, versao);
            layoutCentro.setAlignment(Pos.CENTER);
            layoutCentro.setPadding(new Insets(30));

            BorderPane contentLayout = new BorderPane();
            contentLayout.setCenter(layoutCentro);

            BorderPane.setMargin(versao, new Insets(10));

            Image backgroundImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/static/imagens/backgroundLogin.png")));
            ImageView backgroundImageView = new ImageView(backgroundImage);
            backgroundImageView.fitWidthProperty().bind(contentLayout.widthProperty());
            backgroundImageView.fitHeightProperty().bind(contentLayout.heightProperty());
            backgroundImageView.setPreserveRatio(false); // Estica a imagem para preencher

            // Cria um StackPane para empilhar o fundo e o conteúdo
            StackPane root = new StackPane();
            root.getChildren().addAll(backgroundImageView, contentLayout);

            btnLoginSuccess.setOnAction(e -> {
                Usuario usuarioLogin = new Usuario();
                usuarioLogin.setNome(usuario.getText());
                usuarioLogin.setSenha(senha.getPassword());

                Utils.runWithLoadingResult(root,
                        // tarefa em background (retorna boolean)
                        () -> usuarioService.validacaoLogin(usuarioLogin.getNome(), usuarioLogin.getSenha()),

                        // callback na UI thread com o resultado
                        sucesso -> {
                            if (sucesso) {
                                onLoginSuccess.run();
                            } else {
                                String headerMensagem = "Informação de login";
                                String mensagemErro = "Usuário e/ou senha incorretos. Tente novamente.";

                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Erro de Login");
                                alert.setHeaderText(headerMensagem);
                                alert.setContentText(mensagemErro);
                                alert.initOwner(btnLoginSuccess.getScene().getWindow());
                                alert.showAndWait();
                            }
                        }
                );
            });

            Scene scene = new Scene(root, 500, 400);
            scene.getStylesheets().add(
                    Objects.requireNonNull(getClass().getResource("/static/css/login-styles.css")).toExternalForm()
            );
            return scene;
        }
    }

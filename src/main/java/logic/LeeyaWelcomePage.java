package logic;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.time.LocalTime;
import java.time.ZoneId;

public class LeeyaWelcomePage extends Application {

    @Override
    public void start(Stage stage) {
        // --- Root with vertical spacing ---
        VBox root = new VBox(30);
        root.setPadding(new Insets(40));
        root.setAlignment(Pos.TOP_CENTER);
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #FDEEFF, #E0EBFF);");

        // --- Greeting based on GMT+8 ---
        String greetingText = getGreetingByTime();
        Label greeting = new Label(greetingText);
        greeting.setFont(Font.font("Segoe UI", 18));
        greeting.setTextFill(Color.web("#6A2EFF"));

        Label welcome = new Label("Welcome back to your personal journal space ✨");
        welcome.setFont(Font.font("Segoe UI", 16));
        welcome.setTextFill(Color.web("#7A3DFF"));

        // --- Cards container ---
        HBox cardsContainer = new HBox(20);
        cardsContainer.setAlignment(Pos.CENTER);

        // --- Card 1 ---
        VBox card1 = createCard("📖", "My Journals",
                "Create, edit, and view your journal entries. Express your thoughts and feelings freely.",
                "Open Journals →");

        // --- Card 2 ---
        VBox card2 = createCard("📈", "Weekly Mood Summary",
                "Track your emotional journey and see how your mood changes throughout the week.",
                "View Summary →");

        cardsContainer.getChildren().addAll(card1, card2);

        // --- Footer quote ---
        Label footer = new Label("\"Every day is a new page in your story. Make it a beautiful one.\" 💕");
        footer.setFont(Font.font("Segoe UI Italic", 14));
        footer.setTextFill(Color.web("#A87AFF"));
        footer.setAlignment(Pos.CENTER);

        // --- Assemble root ---
        root.getChildren().addAll(greeting, welcome, cardsContainer, footer);

        Scene scene = new Scene(root, 800, 600);
        stage.setTitle("Smart Journal Dashboard");
        stage.setScene(scene);
        stage.show();
    }

    // --- Helper method to create cards ---
    private VBox createCard(String icon, String titleText, String bodyText, String actionText) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(20));
        card.setPrefWidth(320);
        card.setAlignment(Pos.TOP_LEFT);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 20;");
        card.setEffect(new DropShadow(8, Color.rgb(200, 200, 200, 0.2)));

        Label iconLabel = new Label(icon);
        iconLabel.setFont(Font.font(28));

        Label title = new Label(titleText);
        title.setFont(Font.font("Segoe UI", 16));
        title.setTextFill(Color.web("#5A2EFF"));

        Label body = new Label(bodyText);
        body.setFont(Font.font("Segoe UI", 14));
        body.setTextFill(Color.web("#7A5CFF"));
        body.setWrapText(true);

        Label action = new Label(actionText);
        action.setFont(Font.font("Segoe UI", 14));
        action.setTextFill(Color.web("#A24FFF"));

        card.getChildren().addAll(iconLabel, title, body, action);

        // Hover effect
        card.setOnMouseEntered(e -> card.setStyle("-fx-background-color: #F3E8FF; -fx-background-radius: 20;"));
        card.setOnMouseExited(e -> card.setStyle("-fx-background-color: white; -fx-background-radius: 20;"));

        return card;
    }

    // --- Get greeting based on time in GMT+8 ---
    private String getGreetingByTime() {
        LocalTime now = LocalTime.now(ZoneId.of("GMT+8"));

        if (now.isBefore(LocalTime.NOON)) {
            return "✨ Good Morning!";
        } else if (now.isBefore(LocalTime.of(18, 0))) {
            return "✨ Good Afternoon!";
        } else {
            return "✨ Good Evening!";
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

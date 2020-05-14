import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.StackPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import javafx.stage.*;
import Lex.*;
import Parser.*;
import Opt.*;
import Assemble.*;
import java.io.*;
import java.util.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.BufferedWriter;

public class UI extends Application
{
   public static void main(String [] args)
   {
      launch(args);
   }
   
   @Override
   public void start(Stage ps) throws Exception
   {
      //String src = "pgm1.txt";
      final String TKN = "TokenList.txt";
      final String MOD = "ModTokenList.txt";
      final String QUD = "NewQuads.txt";
      final String N1 = "grammer.txt";
      final String N2 = "tnt.txt";
      final String ST = "SymbolTable.txt";
      final boolean scrn = true;
      
      FileChooser fc = new FileChooser();
      Tooltip tt1 = new Tooltip("Compile your Program");
      Tooltip tt2 = new Tooltip("Create a New File");
      Tooltip tt3 = new Tooltip("Save a File");
      Tooltip tt4 = new Tooltip("Load a File");
      Tooltip tt5 = new Tooltip("Enter Full Screen Mode");
      ps.setTitle("Compiler by Timothy Manno");
      TextArea ta = new TextArea();
      ToolBar tools = new ToolBar();
      Button compileBtn = new Button("Compile");
      compileBtn.setTooltip(tt1);
      compileBtn.setPrefSize(120,40);
      Button newBtn = new Button("New");
      newBtn.setTooltip(tt2);
      newBtn.setPrefSize(120,40);
      Button saveBtn = new Button("Save");
      saveBtn.setTooltip(tt3);
      saveBtn.setPrefSize(120,40);
      Button loadBtn = new Button("Load");
      loadBtn.setTooltip(tt4);
      loadBtn.setPrefSize(120,40);
      Button screenSize = new Button("Full Screen");
      screenSize.setTooltip(tt5);
      screenSize.setPrefSize(120,40);
      tools.getItems().add(compileBtn);
      tools.getItems().add(newBtn);
      tools.getItems().add(saveBtn);
      tools.getItems().add(loadBtn);
      tools.getItems().add(screenSize);
      
      compileBtn.setOnAction(new EventHandler<ActionEvent>()
      {
         @Override
         public void handle(ActionEvent event)
         {
            try
            {
               Lexi lex = new Lexi(ta.getText());
               FileProcessor<Symbol> fps = new FileProcessor<>(ST, lex.getSymTab());
               FileProcessor<Token> fpt = new FileProcessor<>(TKN, lex.getTokenList());
               FileProcessor<Token> fpm = new FileProcessor<>(MOD, lex.getModTokList());
               Parse p = new Parse(lex.getModTokArray());
               FileProcessor<Quads> fpo = new FileProcessor<>(QUD, p.getQuads());
               Assembler asm = new Assembler(p.getQuadsArray(), lex.getSymTabArray());
            }
            catch (FileNotFoundException fnfe)
            {
               System.err.println(fnfe);
            }
            catch (IOException ioe)
            {
               System.err.println(ioe);
            }
         }
      });
      
      saveBtn.setOnAction(e ->
      {
         FileChooser fc1 = new FileChooser();
         File file = fc1.showSaveDialog(ps);
         
         if (file != null)
         {
            try
            {
               PrintWriter pw = new PrintWriter(file);
               pw.println(ta.getText());
               pw.close();
            }
            catch (IOException ioe) { };
         }
      });
      
      loadBtn.setOnAction(e ->
      {
         try
         {
            String data = "";
            String temp = "";
            File selectFile = fc.showOpenDialog(ps);
            fc.setInitialDirectory(selectFile);
            Scanner file = new Scanner(selectFile);
            while (file.hasNextLine())
            {
               temp = file.nextLine();
               data += temp + "\r\n";
            }
            ta.setText(data);
            file.close();
         } catch (FileNotFoundException fnfe) { }
      });
      
      newBtn.setOnAction(new EventHandler<ActionEvent>()
      {
         @Override
         public void handle(ActionEvent event)
         {
            ta.setText("");
         }
      });
      
      screenSize.setOnAction(new EventHandler<ActionEvent>()
      {
         boolean flag = scrn;
         @Override
         public void handle(ActionEvent event)
         {
            if (flag)
            {
               // full screen activated
               ps.setFullScreen(flag);
               screenSize.setText("Regular");
               tt5.setText("Exit Full Screen Mode");
               flag = false;
            }
            else
            {
               // not full screen
               ps.setFullScreen(flag);
               ps.setWidth(700);
               ps.setHeight(700);
               screenSize.setText("Full Screen");
               tt5.setText("Enter Full Screen Mode");
               flag = true;
            }
         }
      });
      
      VBox vb = new VBox(tools,ta);
      Scene scene = new Scene(vb, 700, 700);
      ps.setScene(scene);
      ps.show();
   }
}
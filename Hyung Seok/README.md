SampleController.java
===============

~~~java
package application;



import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;



public class SampleController implements Initializable {

	

	// variables 

	Socket socket;

    //TextArea textArea;

    

	 public GraphicsContext gcb, gcf; // canvas에 색 출력  gcf-canvas gcb-canvasef 

	 public boolean freedesign = true, erase = false, drawline = false,

			 drawoval = false,drawrectangle = false, fill = false; //true false로 키고 끄기

	 double startX=0, startY=0, lastX=0,lastY=0,oldX=0,oldY=0,holdX=0,holdY=0;

	 double hg;

	 int strokeNow=5;

	 String Ids="",Chats="";

	 String IPs="127.0.0.1", Ports="1010";

	 int Port1=1010;

	 int ChatStart=0;

	 double rX1, rY1,  rX2, rY2;

	 int colorNow;

	 Paint colors=Color.rgb(0,0,0),rcolors=Color.rgb(0,0,0);

	 String colorS=colors.toString();

	 double sliders=5,rsliders;

	//Color.rgb(244,244,244); // 그림판 배경 색 

	 

	String Msg;

	 

	 

	 // FXML

	

	@FXML 

	 public TextField Answer,Talk;

	

	 public Canvas canvas, canvasef;

	 public Button Login,Send, Pencil;

	 public Button EndConnect,Clear,Eraser;

	 public Button oval,line,rect;

	 public ColorPicker colorpick;

	 public RadioButton strokeRB,fillRB;

	 public Slider sizeSlider;

	 public TextArea TalkBoard;

	 

	 public ListView PlayerList;

	 public TextField ID,IP,Port;

	 

	 

	

	 

	@FXML

		public void onMousePressedListener(MouseEvent e){ //직선 및 도형 그릴 때 시작 끝 저장 

			this.startX = e.getX();

			this.startY = e.getY();	

		}

	 @FXML

	    public void onMouseDraggedListener(MouseEvent e){ // 마우스 움직임 저장

	        this.lastX = e.getX();

	        this.lastY = e.getY();

	        	// 드래그 할 때 함수들 호출 및 알고리즘 

	     if (Ids.equals("teacher")) {

	        if(drawrectangle)

                drawRectEffect();

	        if(drawoval)

	            drawOvalEffect();

	        if(drawline)

	            drawLineEffect();

	        if(freedesign)

	   	        sendPensil();

	        if(erase)

	        	sendErase();

	     }

	    }

	  @FXML 

	    public void onMouseReleaseListener(MouseEvent e){ 

		 

		  if (Ids.equals("teacher")) {

	        if(drawrectangle)

	            sendRect();

	        if(drawoval)

	            sendOval();

	        if(drawline)

	            sendLine();

	        if(erase) ;

	        	//sendErase();

		  } 

	    }

	  @FXML 

	    public void onMouseEnteredListener(MouseEvent e){

		  this.holdX = e.getX();

		  this.holdY = e.getY();

//		  if(erase) {

//			  eraseEffect();

//		  }

	  

	  }

	  

	

	  

	  @FXML

	    public void onMouseExitedListener(MouseEvent event)

	    { //실험

//	        System.out.println("mouse exited");

	    }

	  

	  // draw method

	

	  public void sendPensil() // 마우스 이용 그리기  메소드 

	    {

		  if (Ids.equals("teacher")) {

	        gcb.setStroke(colorpick.getValue());

	        colors=colorpick.getValue();

	        colorS=colors.toString();

	        sliders=5;

	       

	      	send("Pencil:" + startX + "," + startY+ "," +  lastX + "," + lastY+ "," + sliders+ "," +colorS);

	        

	      	startX=lastX;

	      	startY=lastY;

		  }       

	    }
	  
//	  public void send(MouseEvent e) {
//		  colors = colorpick.getValue();
//		  
//	  }

	  
	  public void sendfill(MouseEvent e) {
		send("Fill:"+true);}

	    private void sendRect() //사각형 그리는 메소드 

	    {

	    	 if (Ids.equals("teacher")) { 

	            gcb.setStroke(colorpick.getValue());

	            colors=colorpick.getValue();

	            colorS=colors.toString();

	            sliders=5;

	            

		      	send("Rect:" + startX + "," + startY+ "," + lastX + "," + lastY+ "," + sliders+ "," +colorS);

	        } 

      

	    }



	    private void sendLine() //선 그리는 메소드 

	    {	

	    	 if (Ids.equals("teacher")) {

	    	

	        	gcb.setStroke(colorpick.getValue());

	            colors=colorpick.getValue();

	            colorS=colors.toString();

	            sliders=5;

	            

		      	send("Line:" + startX + "," + startY+ "," + lastX + "," + lastY+ "," + sliders+ "," +colorS);

	        } 

	    }

	  

	

	  	private void sendOval() //타원 그리는 메소드 

	    {

	  		 if (Ids.equals("teacher")) {

	            gcb.setStroke(colorpick.getValue());
	            
	            

	            colors=colorpick.getValue();

	            colorS=colors.toString();

	            sliders=5;

	            

	            send("Oval:" + startX + "," + startY+ "," + lastX + "," + lastY+ "," + sliders+ "," +colorS);

	        } 

	    }



	  	

	  	 private void sendErase() { 

	  		 if (Ids.equals("teacher")) { 

	  		    colors=Color.rgb(255,255,255);

	  		    colorS=colors.toString();

	            sliders=10;

	      

	        	send("Erase:" + startX + "," + startY+ "," +  lastX + "," + lastY+ "," + sliders+ "," +colorS);

	        	

	        	startX=lastX;

		      	startY=lastY;

	        	

	        }    

	  	 }

	  	 

	  	 public void drawPencil() // 마우스 이용 그리기  메소드 

		    {

	  		    

	         	gcb.setStroke(rcolors);
	         	gcb.setLineWidth(sizeSlider.getValue());
		    	gcb.setLineWidth(rsliders);

		        gcb.strokeLine(rX1, rY1, rX2, rY2);

		

	

		    } 

	  	 public void drawErase() // 마우스 이용 그리기  메소드 

		    {

		    	

	         	gcb.setStroke(rcolors);

		    	gcb.setLineWidth(rsliders);

		        gcb.strokeLine(rX1, rY1, rX2, rY2);

		

	

		    } 

	    private void drawRect() //사각형 그리는 메소드 

	    {

	        double wh = rX2 - rX1;

	        double hg = rY2 - rY1;

	        

	        gcb.setStroke(rcolors);

	    	gcb.setLineWidth(rsliders);

	    	

	        if(fill){

	        	gcf.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
//	        	gcb.setFill(rcolors);
	        	gcb.setFill(colorpick.getValue());
	            gcb.fillRect(rX1, rY1, wh, hg);

	        }else{

	        	gcf.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

	            gcb.strokeRect(rX1, rY1, wh, hg);

	            

	            

	        }

	    }



	    private void drawLine() //선 그리는 메소드 

	    {	

	    	    gcb.setStroke(colorpick.getValue());

	    		gcb.setStroke(rcolors);

		    	gcb.setLineWidth(rsliders);	

	    	    gcf.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

	            gcb.strokeLine(rX1,rY1, rX2, rY2);

	    	

	    }

	    

	    private void drawOval() //타원 그리는 메소드 

	    {

	    	double wh = rX2 - rX1;

	        double hg = rY2 - rY1;

	        gcb.setStroke(rcolors);

	    	gcb.setLineWidth(rsliders);



	        if(fill){

	        	gcf.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
	        	gcb.setFill(colorpick.getValue());
	        	gcb.setFill(rcolors);
	            gcb.fillOval(rX1, rY1, wh, hg);

	        }else{

	        	gcf.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

	            gcb.strokeOval(rX1, rY1, wh, hg);

	        }

	    }



	    

	    @FXML

	    private void setColorChange(ActionEvent e)

	    {

	    	gcb.setStroke(colorpick.getValue());

	    	colors=colorpick.getValue();

	    	colorS=colors.toString();

	   	    	

	    }

	    

	    @FXML

	    private void setSliderChange(MouseEvent e)

	    {

	    	sliders=sizeSlider.getValue();
	    	
	    	send("Size:"+sliders);
	    }

	

	    private void clearsCanvas()

	    {

	        gcf.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

	        gcb.clearRect(0, 0, canvasef.getWidth(), canvasef.getHeight());

	    }

	    

	     // 도형 그릴 때 효과 

	    

	    

	    private void drawOvalEffect()

	    {

	        double wh = lastX - startX;

	        double hg = lastY - startY;

	        gcf.setLineWidth(sizeSlider.getValue());



	        if(fillRB.isSelected()){

	            gcf.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

	            gcf.setFill(colorpick.getValue());

	            gcf.fillOval(startX, startY, wh, hg);

	          

	          

	        }else{

	            gcf.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

	            gcf.setStroke(colorpick.getValue());

	            gcf.strokeOval(startX, startY, wh, hg );

	     

	          

	        }

	       }



	    private void drawRectEffect()

	    {

	        double wh = lastX - startX;

	        double hg = lastY - startY;

	        gcf.setLineWidth(sizeSlider.getValue());



	        if(fillRB.isSelected()){

	            gcf.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

	            gcf.setFill(colorpick.getValue());

	            gcf.fillRect(startX, startY, wh, hg);

	          

	        }else{

	            gcf.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

	            gcf.setStroke(colorpick.getValue());

	            gcf.strokeRect(startX, startY, wh, hg );

	          

	        

	        }

	    }

	    

	    

	    private void drawLineEffect()

	    {

	        gcf.setLineWidth(sizeSlider.getValue());

	        gcf.setStroke(colorpick.getValue());

	        gcf.clearRect(0, 0, canvas.getWidth() , canvas.getHeight());

	        gcf.strokeLine(startX, startY, lastX, lastY);

	     

	     

	    }  

	 

	   

	    

	    @FXML

	    private void setOvalAsCurrentShape(ActionEvent e)

	    {

	        drawline = false;

	        drawoval = true;

	        drawrectangle = false;

	        freedesign = false;

	        erase = false;

	        gcb.setStroke(colorpick.getValue());

	    }



	     @FXML

	    private void setLineAsCurrentShape(ActionEvent e)

	    {

	        drawline = true;

	        drawoval = false;

	        drawrectangle = false;

	        freedesign = false;

	        erase = false;

	        gcb.setStroke(colorpick.getValue());

	    }

	     @FXML

	    private void setRectangleAsCurrentShape(ActionEvent e)

	    {

	        drawline = false;

	        drawoval = false;

	        freedesign = false;

	        erase=false;

	        drawrectangle = true;

	        gcb.setStroke(colorpick.getValue());

	    }

	     	 

	    @FXML 

	    private void clearCanvas(ActionEvent e)

	    {

	        //gcf.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

	        //gcb.clearRect(0, 0, canvasef.getWidth(), canvasef.getHeight());

	    	send("Clear:");

	    	//gcb.setStroke(colorpick.getValue());

	    }

	    

	    @FXML

	    public void setErase(ActionEvent e)

	    {

	        drawline = false;

	        drawoval = false;

	        drawrectangle = false;    

	        erase = true;

	        freedesign= false;

	       // gcb.setStroke(colorpick.getValue());

	    }



	    @FXML

	    public void setFreeDesign(ActionEvent e)

	    {

	        drawline = false;

	        drawoval = false;

	        drawrectangle = false;    

	        erase = false;

	        freedesign = true;

	        gcb.setStroke(colorpick.getValue());

	        

	    }

    

		

		

		

		

		

		@FXML 

	    private void inputID(ActionEvent e)

	    {

			

		 Ids = ID.getText();

		 PlayerList.getItems().add(Ids);

		 

		 if (Ids !="") {

	                Port1 = 1010;

	               // Port1 = Integer.parseInt(Port.getText());

                  //  IPs= IP.getText();

	                

	                startClient(IPs, Port1);

	   

	                //ChatStart=1;

	                

	                Login.setDisable(true);

	                Send.setDisable(false);

	                Talk.setDisable(false);

	                EndConnect.setDisable(false);

	                Talk.requestFocus();

	                

	                //Talk.setText("[알림] "+ Ids +" 님이  입장하였습니다!!\n");

	                TalkBoard.appendText("<알림> "+ Ids +" 님이  입장 하였습니다!!\n");

	                //Send.onActionProperty();

	                //Send.fire();

	                send("<알림> "+ Ids +" 님이  입장 하였습니다!!\n");

	                

	                if (Ids.equals("teacher")) { 

	                	Pencil.setVisible(true);

	                	oval.setVisible(true);

	                	line.setVisible(true);

	                	rect.setVisible(true);

	                	Clear.setVisible(true);

	                	Eraser.setVisible(true);

	                	colorpick.setVisible(true);
	                	
	                	sizeSlider.setVisible(true);
	                	
	                	 fillRB.setVisible(true);
	                	
	                	
	                }

	                

	                

	                

	      }else {

	                stopClient();

	                TalkBoard.appendText("<알림> "+ Ids +" 님이  퇴장 하였습니다!!\n");

	                Login.setDisable(false);

	                Send.setDisable(true);

	                Talk.setDisable(true);

	                

	      }

		 

		 //send("[알림] "+ Ids +" 님이  입장 하였습니다!!\n");

	    }

	

		 @FXML 

		 private void InputChat(ActionEvent e)

		 {

			 

			   Chats = Talk.getText();

				

			   send("["+Ids+"] "+Chats+"\n");

			

			   Talk.setText("");

	           Talk.requestFocus();

			       

		 } 

		 

		 

		 @FXML 

		 private void endConnect(ActionEvent e)

		 {

				

			TalkBoard.appendText("<알림> "+ Ids +" 님이  퇴장하였습니다!!\n");

			Talk.setText("");

			

			stopClient() ;

			

			//int selectedItem = PlayerList.getSelectionModel().getSelectedIndex();

			//PlayerList.getItems().remove(selectedItem);

            //PlayerList.getSelectionModel().select(PlayerList.getItems().size() - 1);

            

           // Object obj = PlayerList.getSelectionModel().getSelectedItem();

           // Ids=obj.toString();

           // ID.setText(Ids);

            

            Login.setDisable(false);

            Send.setDisable(true);

			Talk.setDisable(true);

			EndConnect.setDisable(true);

		 } 

		 

		 @Override

			public void initialize(URL url, ResourceBundle rb) {

				// TODO Auto-generated method stub

				gcf = canvas.getGraphicsContext2D();

				gcb = canvasef.getGraphicsContext2D();

				

				Login.setDisable(false);

	            Send.setDisable(true);

	            Talk.setDisable(true);

	            TalkBoard.setEditable(false);

	 		    PlayerList.setEditable(false); 

	 		    EndConnect.setDisable(true);

	 		    sizeSlider.setVisible(false);

	 		    fillRB.setVisible(false);

	 		    Pencil.setVisible(false);

             	oval.setVisible(false);

              	line.setVisible(false);

             	rect.setVisible(false);

             	Clear.setVisible(false);

             	Eraser.setVisible(false);

             	colorpick.setVisible(false);

	 		    

	 		   //ds.requestFocus();

	 		    

	 		    colorpick.setValue(Color.rgb(0,0,0));

	 		    

				PlayerList.setItems(FXCollections.observableArrayList());

				

				PlayerList.setOnMouseClicked((MouseEvent)->{

		            Object obj = PlayerList.getSelectionModel().getSelectedItem();

		            Ids=obj.toString();

		            ID.setText(Ids);

		            

		        Login.requestFocus();

		        });

			}	

		 

		 public void receiveMsg() {

			 

			 //String parts[];

				String[] pars = Msg.split(":");

				if(pars[0].equals("Size")) {
					rsliders = Double.parseDouble(pars[1]);
				}
				
				if(pars[0].equals("Fill")) {
//					 send("Fill:"+true);
				
					fill = Boolean.parseBoolean(pars[1]);
					System.out.print(fill);
				}

				if (pars[0].equals("Pencil")) {

					pars[1].split(",");

					

					rX1 = Double.parseDouble(pars[1].split(",")[0]);

					rY1 = Double.parseDouble(pars[1].split(",")[1]);

					rX2 = Double.parseDouble(pars[1].split(",")[2]);

					rY2 = Double.parseDouble(pars[1].split(",")[3]);

//					rsliders=Double.parseDouble(pars[1].split(",")[4]);

					rcolors= Color.web(pars[1].split(",")[5]);

						

					drawPencil();

					

				} else if (pars[0].equals("Erase")) {

						pars[1].split(",");

						

						rX1 = Double.parseDouble(pars[1].split(",")[0]);

						rY1 = Double.parseDouble(pars[1].split(",")[1]);

						rX2 = Double.parseDouble(pars[1].split(",")[2]);

						rY2 = Double.parseDouble(pars[1].split(",")[3]);

//						rsliders=Double.parseDouble(pars[1].split(",")[4]);

						rcolors= Color.web(pars[1].split(",")[5]);

							

						drawErase();

				 

				} else if (pars[0].equals("Line")) {

					rX1 = Double.parseDouble(pars[1].split(",")[0]);

					rY1 = Double.parseDouble(pars[1].split(",")[1]);

					rX2 = Double.parseDouble(pars[1].split(",")[2]);

					rY2 = Double.parseDouble(pars[1].split(",")[3]);

//					rsliders=Double.parseDouble(pars[1].split(",")[4]);

					rcolors= Color.web(pars[1].split(",")[5]);

					

					drawLine();

					

				} else if (pars[0].equals("Oval")) {

					rX1 = Double.parseDouble(pars[1].split(",")[0]);

					rY1 = Double.parseDouble(pars[1].split(",")[1]);

					rX2 = Double.parseDouble(pars[1].split(",")[2]);

					rY2 = Double.parseDouble(pars[1].split(",")[3]);

//					rsliders=Double.parseDouble(pars[1].split(",")[4]);

					rcolors= Color.web(pars[1].split(",")[5]);

					

					drawOval();

					

				} else if (pars[0].equals("Rect")) {

					rX1 = Double.parseDouble(pars[1].split(",")[0]);

					rY1 = Double.parseDouble(pars[1].split(",")[1]);

					rX2 = Double.parseDouble(pars[1].split(",")[2]);

					rY2 = Double.parseDouble(pars[1].split(",")[3]);

//					rsliders=Double.parseDouble(pars[1].split(",")[4]);

					rcolors= Color.web(pars[1].split(",")[5]);

					

					drawRect();

					

				} else if (pars[0].equals("Clear")) {

	

					clearsCanvas();

					

				} else if (pars[0].equals("Connected")) {

					//clearsCanvas();

					

				} else if (pars[0].equals("EndConnect")) {

					//clearsCanvas();

				} 

		 }

		 

		 

		 public void startClient(String IP, int port) {

		        Thread thread = new Thread() {

		            public void run() {

		                try {

		                    socket = new Socket(IP, port);

		                    receive();

		                    

		                } catch (Exception e) {

		                    // TODO: handle exception

		                    if (!socket.isClosed()) {    

		                        stopClient();

		                        TalkBoard.appendText("[서버 접속 실패]\n");

		                        Platform.exit();// 프로그램 종료

		                    }

		                }

		            }

		        };

		        thread.start();

		        

		    }

		 

		    // 클라이언트 종료 메소드

		    public void stopClient() {

		        try {

		            if (socket != null && !socket.isClosed()) {

		                socket.close();

		            }

		        } catch (Exception e) {

		            e.printStackTrace();

		        }

		    }

		 

		    // 서버로부터 메세지를 전달받는 메소드

		    public void receive() {

		        while (true) {

		            try {

		                InputStream in = socket.getInputStream();

		                byte[] buffer = new byte[512];

		                int length = in.read(buffer);

		                if (length == -1)

		                    throw new IOException();

		                String message = new String(buffer, 0, length, "UTF-8");

		                

		                

		                

		                if (message.contains(":")) {

		                	Msg=message;

		                	receiveMsg();

		                	

		                	

						}else {

		                	   Platform.runLater(() -> {

		                	    TalkBoard.appendText(message);

		                       });

		                    

		                }

		            } catch (Exception e) {

		                // TODO: handle exception

		                stopClient();

		                break;

		            }

		        }

		    }

		 

		    // 서버로 메세지를 보내는 메소드

		    public void send(String message) {

		        Thread thread1 = new Thread() {

		            public void run() {

		                try {

		                    OutputStream out = socket.getOutputStream();

		                    byte[] buffer = message.getBytes("UTF-8");

		                    out.write(buffer);

		                    out.flush();

		                } catch (Exception e) {

		                    // TODO: handle exception

		                    stopClient();

		                }

		            }

		        };

		        thread1.start();

		    }

		 

		    // 동작 메소드

		  

		 

		

		 

	   



	    

}
~~~

Sample.fxml

~~~fxml
<?xml version="1.0" encoding="UTF-8"?>

<?import javafx.scene.Cursor?>
<?import javafx.scene.canvas.Canvas?>
<?import javafx.scene.control.Button?>
<?import javafx.scene.control.ColorPicker?>
<?import javafx.scene.control.Label?>
<?import javafx.scene.control.ListView?>
<?import javafx.scene.control.RadioButton?>
<?import javafx.scene.control.Slider?>
<?import javafx.scene.control.TextArea?>
<?import javafx.scene.control.TextField?>
<?import javafx.scene.control.ToolBar?>
<?import javafx.scene.effect.ColorAdjust?>
<?import javafx.scene.layout.AnchorPane?>
<?import javafx.scene.layout.HBox?>
<?import javafx.scene.layout.Pane?>
<?import javafx.scene.shape.Rectangle?>
<?import javafx.scene.text.Font?>

<AnchorPane fx:id="result" prefHeight="800.0" prefWidth="1280.0" xmlns="http://javafx.com/javafx/8.0.171" xmlns:fx="http://javafx.com/fxml/1" fx:controller="application.SampleController">

   <children>

      <TextField fx:id="Talk" layoutX="247.0" layoutY="749.0" onAction="#InputChat" prefHeight="29.0" prefWidth="780.0" />

      <TextArea fx:id="TalkBoard" layoutX="247.0" layoutY="531.0" prefHeight="201.0" prefWidth="780.0" />

      <ListView fx:id="PlayerList" layoutX="26.0" layoutY="380.0" prefHeight="397.0" prefWidth="197.0" />

      <Label layoutX="26.0" layoutY="331.0" prefHeight="43.0" prefWidth="197.0" text="접속자" textOverrun="CLIP" />

      <Rectangle arcWidth="5.0" blendMode="ADD" fill="WHITE" height="237.0" layoutX="27.0" layoutY="76.0" smooth="false" stroke="BLACK" strokeLineCap="ROUND" strokeLineJoin="ROUND" strokeMiterLimit="0.0" strokeType="INSIDE" width="197.0">

         <cursor>

            <Cursor fx:constant="CROSSHAIR" />

         </cursor>

      </Rectangle>

      <Pane fx:id="pane" layoutX="248.0" layoutY="67.0" prefHeight="414.0" prefWidth="1019.0" style="-fx-background-color: WHITE;">

         <children>

            <Canvas fx:id="canvasef" height="370.0" layoutY="45.0" width="1018.0" />

            <HBox prefHeight="45.0" prefWidth="1019.0">

               <children>

                  <ToolBar cacheHint="SPEED" prefHeight="43.0" prefWidth="1021.0" snapToPixel="false">

                    <items>

                      <Button fx:id="Pencil" mnemonicParsing="false" onAction="#setFreeDesign" text="연필">

                           <font>

                              <Font name="Cambria Math" size="15.0" />

                           </font>

                        </Button>

                        <Button fx:id="oval" mnemonicParsing="false" onAction="#setOvalAsCurrentShape" text="타원" />

                        <Button fx:id="line" mnemonicParsing="false" onAction="#setLineAsCurrentShape" text="직선" />

                        <Button fx:id="rect" mnemonicParsing="false" onAction="#setRectangleAsCurrentShape" text="직사각형" />

                        <Button fx:id="Eraser" mnemonicParsing="false" onAction="#setErase" text="지우개" />

                        <Button fx:id="Clear" mnemonicParsing="false" onAction="#clearCanvas" text="초기화" />

                        <RadioButton fx:id="fillRB" mnemonicParsing="false" onMouseClicked="#sendfill" text="Fill" />

                        <Slider fx:id="sizeSlider" majorTickUnit="5.0" max="20.0" min="1.0" minorTickCount="1" onMousePressed="#setSliderChange" onMouseReleased="#setSliderChange" prefHeight="17.0" prefWidth="110.0" showTickMarks="true" />

                        <ColorPicker fx:id="colorpick" onAction="#setColorChange" />

                    </items>

                  </ToolBar>

               </children>

            </HBox>

            <Canvas fx:id="canvas" height="370.0" layoutY="45.0" onMouseDragged="#onMouseDraggedListener" onMouseEntered="#onMouseEnteredListener" onMouseExited="#onMouseExitedListener" onMousePressed="#onMousePressedListener" onMouseReleased="#onMouseReleaseListener" width="1018.0" />

         </children>

      </Pane>

      <Label layoutX="40.0" layoutY="94.0" text="대화명" />

      <TextField fx:id="ID" layoutX="94.0" layoutY="89.0" onAction="#inputID" prefHeight="28.0" prefWidth="124.0" />

      <Button fx:id="Send" layoutX="1040.0" layoutY="749.0" mnemonicParsing="false" onAction="#InputChat" prefHeight="29.0" prefWidth="92.0" text="전 송" />

      <Label layoutX="248.0" layoutY="502.0" text="대 화 창" />

      <Button fx:id="EndConnect" layoutX="41.0" layoutY="268.0" mnemonicParsing="false" onAction="#endConnect" prefHeight="29.0" prefWidth="171.0" text="접속 종료" />

      <Label layoutX="38.0" layoutY="135.0" text="IP 주소" />

      <TextField fx:id="IP" layoutX="94.0" layoutY="130.0" onAction="#inputID" prefHeight="28.0" prefWidth="124.0" promptText="127.0.0.1" />

      <TextField fx:id="Port" layoutX="94.0" layoutY="173.0" onAction="#inputID" prefHeight="28.0" prefWidth="124.0" promptText="1010" />

      <Label layoutX="39.0" layoutY="178.0" text="포 트" />

      <Button fx:id="Login" layoutX="41.0" layoutY="228.0" mnemonicParsing="false" onAction="#inputID" prefHeight="29.0" prefWidth="171.0" text="로 그 인" />

   </children>

   <effect>

      <ColorAdjust />
  </effect>

</AnchorPane>
~~~


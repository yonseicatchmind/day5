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
    
	 public GraphicsContext gcb, gcf; // canvas�� �� ���  gcf-canvas gcb-canvasef 
	 public boolean freedesign = true, erase = false, drawline = false,
			 drawoval = false,drawrectangle = false; //true false�� Ű�� ����
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
	//Color.rgb(244,244,244); // �׸��� ��� �� 
	 
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
		public void onMousePressedListener(MouseEvent e){ //���� �� ���� �׸� �� ���� �� ���� 
			this.startX = e.getX();
			this.startY = e.getY();
	
		}
	 @FXML
	    public void onMouseDraggedListener(MouseEvent e){ // ���콺 ������ ����
	        this.lastX = e.getX();
	        this.lastY = e.getY();
	        	// �巡�� �� �� �Լ��� ȣ�� �� �˰��� 
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
	    { //����
//	        System.out.println("mouse exited");
	    }
	  
	  // draw method
	
	  public void sendPensil() // ���콺 �̿� �׸���  �޼ҵ� 
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
	  
	   
	    private void sendRect() //�簢�� �׸��� �޼ҵ� 
	    {
	    	 if (Ids.equals("teacher")) { 
	            gcb.setStroke(colorpick.getValue());
	            colors=colorpick.getValue();
	            colorS=colors.toString();
	            sliders=5;
	            
		      	send("Rect:" + startX + "," + startY+ "," + lastX + "," + lastY+ "," + sliders+ "," +colorS);
	        } 
      
	    }

	    private void sendLine() //�� �׸��� �޼ҵ� 
	    {	
	    	 if (Ids.equals("teacher")) {
	    	
	        	gcb.setStroke(colorpick.getValue());
	            colors=colorpick.getValue();
	            colorS=colors.toString();
	            sliders=5;
	            
		      	send("Line:" + startX + "," + startY+ "," + lastX + "," + lastY+ "," + sliders+ "," +colorS);
	        } 
	    }
	  
	
	  	private void sendOval() //Ÿ�� �׸��� �޼ҵ� 
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
	  	 
	  	 public void drawPencil() // ���콺 �̿� �׸���  �޼ҵ� 
		    {
	  		    
	         	gcb.setStroke(rcolors);
		    	gcb.setLineWidth(rsliders);
		        gcb.strokeLine(rX1, rY1, rX2, rY2);
		
	
		    } 
	  	 public void drawErase() // ���콺 �̿� �׸���  �޼ҵ� 
		    {
		    	
	         	gcb.setStroke(rcolors);
		    	gcb.setLineWidth(rsliders);
		        gcb.strokeLine(rX1, rY1, rX2, rY2);
		
	
		    } 
	    private void drawRect() //�簢�� �׸��� �޼ҵ� 
	    {
	        double wh = rX2 - rX1;
	        double hg = rY2 - rY1;
	        
	        gcb.setStroke(rcolors);
	    	gcb.setLineWidth(rsliders);
	    	
	        if(fillRB.isSelected()){
	        	gcf.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
	            gcb.fillRect(rX1, rY1, wh, hg);
	        }else{
	        	gcf.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
	            gcb.strokeRect(rX1, rY1, wh, hg);
	            
	            
	        }
	    }

	    private void drawLine() //�� �׸��� �޼ҵ� 
	    {	
	    	    gcb.setStroke(colorpick.getValue());
	    		gcb.setStroke(rcolors);
		    	gcb.setLineWidth(rsliders);	
	    	    gcf.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
	            gcb.strokeLine(rX1,rY1, rX2, rY2);
	    	
	    }
	    
	    private void drawOval() //Ÿ�� �׸��� �޼ҵ� 
	    {
	    	double wh = rX2 - rX1;
	        double hg = rY2 - rY1;
	        gcb.setStroke(rcolors);
	    	gcb.setLineWidth(rsliders);

	        if(fillRB.isSelected()){
	        	gcf.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
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
	    private void setSliderChange(ActionEvent e)
	    {
	    	
	    	gcb.setLineWidth(sizeSlider.getValue());
	    	sliders=sizeSlider.getValue();
	    	TalkBoard.appendText(String.valueOf(sliders));
	    
	    	
	    }
	
	    private void clearsCanvas()
	    {
	        gcf.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
	        gcb.clearRect(0, 0, canvasef.getWidth(), canvasef.getHeight());
	    }
	    
	     // ���� �׸� �� ȿ�� 
	    
	    
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
	                
	                //Talk.setText("[�˸�] "+ Ids +" ����  �����Ͽ����ϴ�!!\n");
	                TalkBoard.appendText("<�˸�> "+ Ids +" ����  ���� �Ͽ����ϴ�!!\n");
	                //Send.onActionProperty();
	                //Send.fire();
	                send("<�˸�> "+ Ids +" ����  ���� �Ͽ����ϴ�!!\n");
	                
	                if (Ids.equals("teacher")) { 
	                	Pencil.setVisible(true);
	                	oval.setVisible(true);
	                	line.setVisible(true);
	                	rect.setVisible(true);
	                	Clear.setVisible(true);
	                	Eraser.setVisible(true);
	                	colorpick.setVisible(true);
	                }
	                
	                
	                
	      }else {
	                stopClient();
	                TalkBoard.appendText("<�˸�> "+ Ids +" ����  ���� �Ͽ����ϴ�!!\n");
	                Login.setDisable(false);
	                Send.setDisable(true);
	                Talk.setDisable(true);
	                
	      }
		 
		 //send("[�˸�] "+ Ids +" ����  ���� �Ͽ����ϴ�!!\n");
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
				
			TalkBoard.appendText("<�˸�> "+ Ids +" ����  �����Ͽ����ϴ�!!\n");
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
				
				if (pars[0].equals("Pencil")) {
					pars[1].split(",");
					
					rX1 = Double.parseDouble(pars[1].split(",")[0]);
					rY1 = Double.parseDouble(pars[1].split(",")[1]);
					rX2 = Double.parseDouble(pars[1].split(",")[2]);
					rY2 = Double.parseDouble(pars[1].split(",")[3]);
					rsliders=Double.parseDouble(pars[1].split(",")[4]);
					rcolors= Color.web(pars[1].split(",")[5]);
						
					drawPencil();
					
				} else if (pars[0].equals("Erase")) {
						pars[1].split(",");
						
						rX1 = Double.parseDouble(pars[1].split(",")[0]);
						rY1 = Double.parseDouble(pars[1].split(",")[1]);
						rX2 = Double.parseDouble(pars[1].split(",")[2]);
						rY2 = Double.parseDouble(pars[1].split(",")[3]);
						rsliders=Double.parseDouble(pars[1].split(",")[4]);
						rcolors= Color.web(pars[1].split(",")[5]);
							
						drawErase();
				 
				} else if (pars[0].equals("Line")) {
					rX1 = Double.parseDouble(pars[1].split(",")[0]);
					rY1 = Double.parseDouble(pars[1].split(",")[1]);
					rX2 = Double.parseDouble(pars[1].split(",")[2]);
					rY2 = Double.parseDouble(pars[1].split(",")[3]);
					rsliders=Double.parseDouble(pars[1].split(",")[4]);
					rcolors= Color.web(pars[1].split(",")[5]);
					
					drawLine();
					
				} else if (pars[0].equals("Oval")) {
					rX1 = Double.parseDouble(pars[1].split(",")[0]);
					rY1 = Double.parseDouble(pars[1].split(",")[1]);
					rX2 = Double.parseDouble(pars[1].split(",")[2]);
					rY2 = Double.parseDouble(pars[1].split(",")[3]);
					rsliders=Double.parseDouble(pars[1].split(",")[4]);
					rcolors= Color.web(pars[1].split(",")[5]);
					
					drawOval();
					
				} else if (pars[0].equals("Rect")) {
					rX1 = Double.parseDouble(pars[1].split(",")[0]);
					rY1 = Double.parseDouble(pars[1].split(",")[1]);
					rX2 = Double.parseDouble(pars[1].split(",")[2]);
					rY2 = Double.parseDouble(pars[1].split(",")[3]);
					rsliders=Double.parseDouble(pars[1].split(",")[4]);
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
		                        TalkBoard.appendText("[���� ���� ����]\n");
		                        Platform.exit();// ���α׷� ����
		                    }
		                }
		            }
		        };
		        thread.start();
		        
		    }
		 
		    // Ŭ���̾�Ʈ ���� �޼ҵ�
		    public void stopClient() {
		        try {
		            if (socket != null && !socket.isClosed()) {
		                socket.close();
		            }
		        } catch (Exception e) {
		            e.printStackTrace();
		        }
		    }
		 
		    // �����κ��� �޼����� ���޹޴� �޼ҵ�
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
		 
		    // ������ �޼����� ������ �޼ҵ�
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
		 
		    // ���� �޼ҵ�
		  
		 
		
		 
	   

	    
}
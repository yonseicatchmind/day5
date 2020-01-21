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

public class SampleController implements Initializable {
	boolean flag = false;
	// variables 
	Socket socket;
    //TextArea textArea;
	 public GraphicsContext gcb, gcf; // canvas�� �� ���  gcf-canvas gcb-canvasef 
	 public boolean freedesign = true, erase = false; //true false�� Ű�� ����
	 double startX=0, startY=0, lastX=0,lastY=0,oldX=0,oldY=0,holdX=0,holdY=0;
	 double hg;
	 
	 String Ids="",Chats="";
	 String IPs="127.0.0.1", Ports="1010";
	 int Port1=1010;
	 int ChatStart=0;
	 double rX, rY;
//			 c= Color.rgb(244,244,244); // �׸��� ��� �� 
	 // FXML
	@FXML 
	 public TextField Answer,Talk;
	 public Canvas canvas, canvasef;
	 public Button Login,Send, Pencil;
	 public Button EndConnect,Clear,Eraser;
	 public ColorPicker colorpick;
	 public RadioButton strokeRB,fillRB;
	 public Slider sizeSlider;
	 public TextArea TalkBoard;
	 public ListView PlayerList;
	 public TextField ID,IP,Port;
	 public String ans = "";
	@FXML
		public void onMousePressedListener(MouseEvent e){ //���� �� ���� �׸� �� ���� �� ���� 
			this.startX = e.getX();
			this.startY = e.getY();
			//this.oldX = e.getX();
			//this.oldY = e.getY();
			
			oldX=0;
			oldY=0;
			send("Pencil:" + oldX + "," + oldY);
			
		}
	 @FXML
	    public void onMouseDraggedListener(MouseEvent e){ // ���콺 ������ ����
	        this.lastX = e.getX();
	        this.lastY = e.getY();
	        	// �巡�� �� �� �Լ��� ȣ�� �� �˰��� 
	        if(freedesign)
	            freeDrawing();
	        if(erase)
	        	erase();
	    }
	  @FXML 
	    public void onMouseReleaseListener(MouseEvent e){ 
		  rX=0;
		  rY=0;
	        if(erase)
	        	erase();
	      
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
	    public void freeDrawing() // ���콺 �̿� �׸���  �޼ҵ� 
	    {
	    	send("Pencil:" + lastX + "," + lastY); 
	    }
	    
	    public void pencil() // ���콺 �̿� �׸���  �޼ҵ� 
	    {
	    	if (oldX==0) oldX=rX;
	    	if (oldY==0) oldY=rY;
	    	
	    	if (rX!=0) {
	    	
	    	gcf.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
	    	gcb.setStroke(Color.BLACK);
	    	gcb.setLineWidth(5);
	        gcb.strokeLine(oldX, oldY, rX, rY);
	    	}
	        oldX = rX;
	        oldY = rY;
	    }
	    
	    private void erase() { // ���찳 �޼ҵ� 
	    	if(erase) {
	    		gcf.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
//		    	gcb.setLineWidth(5);
		        gcb.setLineWidth(sizeSlider.getValue());
		        gcb.setStroke(Color.WHITE);
		        gcb.strokeLine(oldX, oldY, lastX, lastY);
		       //���콺 �̺�Ʈ���� ��ġ �޾ƿ� 
		        oldX = lastX;
		        oldY = lastY;
	    		}
	    }
	    // button connect 	     	 
	    @FXML 
	    private void clearCanvas(ActionEvent e){
	        gcf.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
	        gcb.clearRect(0, 0, canvasef.getWidth(), canvasef.getHeight());
	    }
	    @FXML
	    public void setErase(ActionEvent e){ 
	        erase = true;
	        freedesign= false;
	    }
	    @FXML
	    public void setFreeDesign(ActionEvent e){   
	        erase = false;
	        freedesign = true;
	    }
		@FXML 
	    private void inputID(ActionEvent e)
	    {
		 Ids = ID.getText();
		 PlayerList.getItems().add(Ids);
		 if (Ids !="") {
	                Port1 = 1010;
	                startClient(IPs, Port1);
	                Login.setDisable(true);
	                Send.setDisable(false);
	                Talk.setDisable(false);
	                EndConnect.setDisable(false);
	                Talk.requestFocus();
	                TalkBoard.appendText("<�˸�> "+ Ids +" ����  ���� �Ͽ����ϴ�!!\n");
	                send("<�˸�> "+ Ids +" ����  ���� �Ͽ����ϴ�!!\n");
	      }else {
	                stopClient();
	                Login.setDisable(false);
	                Send.setDisable(true);
	                Talk.setDisable(true);
	      }
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
		 private void endConnect(ActionEvent e){
			TalkBoard.appendText("<�˸�> "+ Ids +" ����  �����Ͽ����ϴ�!!\n");
			Talk.setText("");
			stopClient() ;
            
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
	 		    
				PlayerList.setItems(FXCollections.observableArrayList());
				
				PlayerList.setOnMouseClicked((MouseEvent)->{
		            Object obj = PlayerList.getSelectionModel().getSelectedItem();
		            Ids=obj.toString();
		            ID.setText(Ids);
		            
		        Login.requestFocus();
		        });
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
							String[] pars = message.split(":");
							if (pars[0].equals("Pencil")) {
								pars = pars[1].split(",");
								rX = Double.parseDouble(pars[0]);
								rY = Double.parseDouble(pars[1]);
								pencil();
							} 
						}else {
		                	   Platform.runLater(() -> {
		                		   if(message.contains("/q")) { //���� ������>>>>> /q���þ� �̷� �������� ä���� �ľ���
		                			   TalkBoard.appendText("["+message.substring((message.indexOf("[")+1), message.indexOf("]")) + "] �� ������ �����Ͽ����ϴ�.\n");
		                			   ans=message.substring(message.lastIndexOf("q")+1, message.lastIndexOf("q")+4); //���þ� ����
		                		   }
		                		   else
		                			   TalkBoard.appendText(message); //���þ� ������ �ƴϸ� �׳� ���
		                		   if(ans.equals(message.substring(message.indexOf("]")+2,message.length()))) { //���þ�� ä���̶� ������
		                			   TalkBoard.appendText("good\n");
		                		   }
		                		   else { // ���þ� ������� �ٸ���
		                			   TalkBoard.appendText("good\n");
		                		   }
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
}
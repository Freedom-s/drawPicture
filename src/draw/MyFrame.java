package draw;

import com.mr.util.FrameGetShape;
import com.mr.util.Shapes;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.AncestorListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author freedom-sjh
 * @data 2022/5/11-15:48
 */
public class MyFrame extends JFrame{
    //在这里添加面板的一些成员属性
    BufferedImage image = new BufferedImage(570,390,BufferedImage.TYPE_INT_BGR);
    Graphics gs = image.getGraphics();//让上面的位图的到此画笔
    Graphics2D g = (Graphics2D) gs;//画笔转化为2d画笔
    MyCanvas myCanvas = new MyCanvas(); //创建画布对象
    Color penColor = Color.black;//默认铅笔颜色为黑色
    Color backgroundColor = Color.white;//默认背景颜色为白色


    //上方按钮组定义
    ButtonGroup buttonGroup;//设置一个按钮组
    JPanel jPanelGroup;
    JButton drawLine;
    JButton drawCircle;
    JButton drawFillCircle;
    JButton drawRect;
    JButton drawFillRect;
    JButton drawPolgon;
    JButton drawFree;


    //菜单栏组件

    JMenuBar jmb = new JMenuBar();
    JMenu fileMenu = new JMenu("文件");
    JMenu selectColorMenu = new JMenu("选择颜色");
    JMenu editMenu = new JMenu("编辑");
    JMenu explainMenu = new JMenu("说明");

    JMenuItem open = new JMenuItem("打开");
    JMenuItem save = new JMenuItem("保存");
    JMenuItem pen = new JMenuItem("画笔颜色");
    JMenuItem background = new JMenuItem("背景颜色");
    JMenuItem clear = new JMenuItem("清除");
    JMenuItem eraser = new JMenuItem("橡皮擦");
    JMenuItem explain = new JMenuItem("关于我们");

    //定义一个画图的状态，当改为true时进入正常的画笔状态
    boolean drawShape = false;
    boolean drawLineState = false;
    //创建一个String对象用来接受点击的是哪个按钮
    String buttonConent = "";

    public void init(){
        g.setColor(backgroundColor);
        g.fillRect(0,0,570,390);
        g.setColor(penColor);
        myCanvas.setImage(image);//把位图对象导入
        //myCanvas.setImage(picImage);
        getContentPane().add(myCanvas);//将画布导入到窗体当中


        //初始化方法中添加这些按钮
        jPanelGroup = new JPanel();
        getContentPane().add(jPanelGroup,BorderLayout.NORTH);
        drawLine = new JButton("直线");
        drawCircle = new JButton("空心圆");
        drawFillCircle = new JButton("实心圆");
        drawRect  = new JButton("矩形");
        drawFillRect = new JButton("实心矩形");
        drawPolgon = new JButton("多边形");
        drawFree =  new JButton("自由绘图");
        jPanelGroup.add(drawLine);
        jPanelGroup.add(drawCircle);
        jPanelGroup.add(drawFillCircle);
        jPanelGroup.add(drawRect);
        jPanelGroup.add(drawFillRect);
        jPanelGroup.add(drawPolgon);
        jPanelGroup.add(drawFree);

        //组装菜单栏
        fileMenu.add(open);
        fileMenu.add(save);
        selectColorMenu.add(pen);
        selectColorMenu.add(background);
        editMenu.add(clear);
        editMenu.add(eraser);
        explainMenu.add(explain);
        jmb.add(fileMenu);
        jmb.add(selectColorMenu);
        jmb.add(editMenu);
        jmb.add(explainMenu);
    }
    public MyFrame(){
        setResizable(true);//不可改变
        setTitle("绘图软件");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(500,100,574,460);
        init();
        //pack();
        setJMenuBar(jmb);
        addListener();
    }
    int x = -1;
    int y = -1;//先将点设置到窗体外
    int preX=-1;
    int preY=-1;
    boolean reaser = false;//橡皮标志



    //画空心圆形的一些成员变量
    int cilckX;
    int cilckY;
    String radius;


    //画矩形的一些成员变量
    String rect;
    //设置监听器
    public void addListener(){
        myCanvas.addMouseMotionListener(new MouseMotionAdapter() {//鼠标拖动事件
            @Override
            public void mouseDragged(MouseEvent e) {
                if(x>0&&y>0){
                    if(reaser==true){//如果是橡皮的话
                        g.setColor(backgroundColor);
                        g.fillRect(x,y,5,5);//以5的小正方形滑动清除
                    }else {
                        g.drawLine(x,y,e.getX(),e.getY());
                    }
                }
                x = e.getX();
                y = e.getY();
                myCanvas.repaint();
            }
        });
        myCanvas.addMouseListener(new MouseAdapter() {//鼠标抬起事件
            @Override
            public void mouseReleased(MouseEvent e) {
                //画笔归为、
                x = -1;
                y = -1;
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if(drawShape){
                    switch(buttonConent){
                        case "空心圆":
                            Integer r = Integer.parseInt(radius) ;
                            cilckX = e.getX();
                            cilckY = e.getY();
                            g.drawOval(cilckX-r,cilckY-r,r*2,r*2);
                            break;
                        case "实心圆":
                            Integer fr = Integer.parseInt(radius) ;
                            cilckX = e.getX();
                            cilckY = e.getY();
                            g.fillOval(cilckX-fr,cilckY-fr,fr*2,fr*2);
                            break;
                        case "矩形":
                            String[] arr =  rect.split(" ");
                            cilckX = e.getX();
                            cilckY = e.getY();
                            int rectWidth = Integer.parseInt(arr[0]);
                            int rectHeight = Integer.parseInt(arr[1]);
                            g.drawRect(cilckX-rectWidth/2,cilckY-rectHeight/2,rectWidth,rectHeight);
                            break;
                        case "实心矩形":
                            String[] farr =  rect.split(" ");
                            cilckX = e.getX();
                            cilckY = e.getY();
                            int rectFillWidth = Integer.parseInt(farr[0]);
                            int rectFillHeight = Integer.parseInt(farr[1]);
                            g.fillRect(cilckX-rectFillWidth/2,cilckY-rectFillHeight/2,rectFillWidth,rectFillHeight);
                            break;
                    }
                }
                myCanvas.repaint();
                drawShape = false;
                buttonConent = "";
            }
        });
        background.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Color bgColor = JColorChooser.showDialog(MyFrame.this,"背景颜色选择",Color.white);
                if(bgColor!=null){
                    backgroundColor = bgColor;
                }
                g.setColor(backgroundColor);
                g.fillRect(0,0,570,390);
                g.setColor(penColor);
                myCanvas.repaint();
            }
        });
        pen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Color pColor = JColorChooser.showDialog(MyFrame.this,"画笔颜色选择",Color.black);
                if(penColor!=null){
                    penColor = pColor;
                }
                g.setColor(penColor);
            }
        });
        clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                g.setColor(backgroundColor);
                g.fillRect(0,0,570,390);
                g.setColor(penColor);
                myCanvas.repaint();
            }
        });
        eraser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(eraser.getText().equals("橡皮擦")){
                    reaser = true;
                    eraser.setText("画图");
                }else {
                    reaser= false;
                    eraser.setText("橡皮擦");
                    g.setColor(penColor);
                }
            }
        });

        //接下来实现按钮组的一些功能
        drawFree.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                drawLineState = false;
                preX = -1;
                preY = -1;
            }
        });
        drawLine.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                drawLineState = true;
                myCanvas.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if(drawLineState){
                            if(preX>0&&preY>0){
                                g.drawLine(preX,preY,e.getX(),e.getY());
                            }
                            preX=e.getX();
                            preY=e.getY();
                            myCanvas.repaint();
                        }
                    }
                });
            }
        });
        drawCircle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                drawShape = true;
                buttonConent = drawCircle.getText();
                radius = JOptionPane.showInputDialog(MyFrame.this,"请输入圆形的半径","画圆形",JOptionPane.PLAIN_MESSAGE);
            }
    });
        drawFillCircle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                drawShape = true;
                buttonConent = drawFillCircle.getText();
                radius = JOptionPane.showInputDialog(MyFrame.this,"请输入圆形的半径","画实心圆",JOptionPane.PLAIN_MESSAGE);
            }
        });
        drawRect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                drawShape = true;
                buttonConent = drawRect.getText();
                rect = JOptionPane.showInputDialog(MyFrame.this,"请输入矩阵的宽和高(中间用空格隔开)","画矩形",JOptionPane.PLAIN_MESSAGE);

            }
        });
        drawFillRect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                drawShape = true;
                buttonConent = drawFillRect.getText();
                rect = JOptionPane.showInputDialog(MyFrame.this,"请输入实心矩阵的宽和高(中间用空格隔开)","画实心矩形",JOptionPane.PLAIN_MESSAGE);
            }
        });
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFileChooser jFileChooser = new JFileChooser(".");
                jFileChooser.showOpenDialog(MyFrame.this);
                File file = jFileChooser.getSelectedFile();
                try {
                    ImageIO.write(image,"JPEG",file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        open.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFileChooser jFileChooser = new JFileChooser(".");
                jFileChooser.showOpenDialog(MyFrame.this);
                File file = jFileChooser.getSelectedFile();
                try {
                    image = ImageIO.read(file);
                    myCanvas.setImage(image);
                    myCanvas.repaint();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        explain.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JOptionPane.showMessageDialog(MyFrame.this,"这是一个简易的绘图软件","关于我们",JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }
    public static void main(String[] args) {
        MyFrame myFrame = new MyFrame();
        myFrame.setVisible(true);

    }
}

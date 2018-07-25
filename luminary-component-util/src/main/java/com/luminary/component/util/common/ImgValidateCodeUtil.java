package com.luminary.component.util.common;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.Random;

import javax.imageio.ImageIO;

public class ImgValidateCodeUtil {

	/**
	 * 生成随机图片，用来作为验证码
	 */
	private int width = 100;// 生成的图片的宽度
	private int height = 30;// 生成的图片的高度
	private int length = 4; // 生成的验证码长度 
	private Color backgroundColor = Color.WHITE; // 生成的图片的背景色
	private Type type = Type.ENGLISH_CHARACTER_NUMBER; // 生成的验证码类型
	
	public static enum Type {
		CHINESE_CHARACTER, ENGLISH_CHARACTER, NUMBER, ENGLISH_CHARACTER_NUMBER
	}
	
	 
	/**
	 * 生成图片验证码
	 * 
	 * @return
	 * @throws Exception
	 */
	public String[] createValiteCode() throws Exception{
		return createImgValidateCode("nl");//生成数字加字母的图片验证码 
	}

	/**
	 * 生成图片验证码
	 *
	 * @param codeType
	 * @throws Exception
	 */
	private String[] createImgValidateCode(String codeType) throws Exception { 
		String[] validateInfoArray=new String[2];
		// 1.在内存中创建一张图片
		BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		// 2.得到图片
		Graphics g = bi.getGraphics();
		// 3.设置图片的背影色
		setBackGround(g);
		// 4.设置图片的边框
		setBorder(g);
		// 5.在图片上画干扰线
		drawRandomLine(g);
		// 6.画干扰点  
        createRandomPoint(this.width, this.height,50,g,100);   
		// 7.写在图片上随机数
		// String random = drawRandomNum((Graphics2D) g,"ch");//生成中文验证码图片
		// String random = drawRandomNum((Graphics2D) g,"nl");//生成数字和字母组合的验证码图片
		// String random = drawRandomNum((Graphics2D) g,"n");//生成纯数字的验证码图片
		// String random = drawRandomNum((Graphics2D) g,"l");//生成纯字母的验证码图片
        String random = drawRandomNum((Graphics2D) g, codeType);// 根据客户端传递的codeType标识生成验证码图片
		// 8.将图片写给浏览器
		ByteArrayOutputStream outStream = new ByteArrayOutputStream(); 
		ImageIO.write(bi, "jpg", outStream);
        byte[] bytes = outStream.toByteArray();  
		String base64String=new String(Base64.getEncoder().encode(bytes)); 
		outStream.close();
		
		validateInfoArray[0]=random;
		validateInfoArray[1]=base64String;
		
		return validateInfoArray;
	}

	/**
	 * 设置图片的背景色
	 * 
	 * @param g
	 */
	private void setBackGround(Graphics g) {
		// 设置颜色
		g.setColor(this.backgroundColor);
		// 填充区域
		g.fillRect(0, 0, width, height);
	}

	/**
	 * 设置图片的边框
	 * 
	 * @param g
	 */
	private void setBorder(Graphics g) {
		// 设置边框颜色
		g.setColor(Color.white);
		// 边框区域
		g.drawRect(1, 1, width - 2, height - 2);
	}

	/**
	 * 在图片上画随机线条
	 * 
	 * @param g
	 */
	private void drawRandomLine(Graphics g) {
		// 设置颜色
		g.setColor(Color.gray);
		// 设置线条个数并画线
		for (int i = 0; i < 5; i++) {
			int x1 = new Random().nextInt(width);
			int y1 = new Random().nextInt(height);
			int x2 = new Random().nextInt(width);
			int y2 = new Random().nextInt(height);
			g.drawLine(x1, y1, x2, y2);
		}
	}
	
	/** 
     * 随机产生干扰点 
     * @param width 
     * @param height 
     * @param many 
     * @param g 
     * @param alpha 透明度0~255 0表示全透 
     */  
    private void createRandomPoint(int width,int height,int many,Graphics g,int alpha)  
    {  // 随机产生干扰点  
        for (int i=0;i<many;i++) {  
            int x = new Random().nextInt(width);   
            int y = new Random().nextInt(height);   
            g.setColor(getColor(alpha));  
            g.drawOval(x,y,new Random().nextInt(3),new Random().nextInt(3));   
        }   
    } 
    
    /*** 随机返回一种颜色,透明度0~255 0表示全透 
     * @return 随机返回一种颜色 
     * @param alpha 透明度0~255 0表示全透 
     */  
    private Color getColor(int alpha)  
    {  
        int R=(int) (Math.random()*255);  
        int G=(int) (Math.random()*255);  
        int B=(int) (Math.random()*255);  
        return new Color(R,G,B,alpha);
    } 
    

	/**
	 * 画随机字符
	 * 
	 * @param g
	 * @param codeType
	 * @return  
	 */
	private String drawRandomNum(Graphics2D g, String codeType) {
		String randomNum="";
		
		// 设置颜色
		g.setColor(Color.blue);
		// 设置字体
		g.setFont(new Font("Fixedsys", Font.BOLD, 25));
		// 常用的中国汉字
		String baseChineseChar = "\u7684\u4e00\u4e86\u662f\u6211\u4e0d\u5728\u4eba\u4eec\u6709\u6765\u4ed6\u8fd9\u4e0a\u7740\u4e2a\u5730\u5230\u5927\u91cc\u8bf4\u5c31\u53bb\u5b50\u5f97\u4e5f\u548c\u90a3\u8981\u4e0b\u770b\u5929\u65f6\u8fc7\u51fa\u5c0f\u4e48\u8d77\u4f60\u90fd\u628a\u597d\u8fd8\u591a\u6ca1\u4e3a\u53c8\u53ef\u5bb6\u5b66\u53ea\u4ee5\u4e3b\u4f1a\u6837\u5e74\u60f3\u751f\u540c\u8001\u4e2d\u5341\u4ece\u81ea\u9762\u524d\u5934\u9053\u5b83\u540e\u7136\u8d70\u5f88\u50cf\u89c1\u4e24\u7528\u5979\u56fd\u52a8\u8fdb\u6210\u56de\u4ec0\u8fb9\u4f5c\u5bf9\u5f00\u800c\u5df1\u4e9b\u73b0\u5c71\u6c11\u5019\u7ecf\u53d1\u5de5\u5411\u4e8b\u547d\u7ed9\u957f\u6c34\u51e0\u4e49\u4e09\u58f0\u4e8e\u9ad8\u624b\u77e5\u7406\u773c\u5fd7\u70b9\u5fc3\u6218\u4e8c\u95ee\u4f46\u8eab\u65b9\u5b9e\u5403\u505a\u53eb\u5f53\u4f4f\u542c\u9769\u6253\u5462\u771f\u5168\u624d\u56db\u5df2\u6240\u654c\u4e4b\u6700\u5149\u4ea7\u60c5\u8def\u5206\u603b\u6761\u767d\u8bdd\u4e1c\u5e2d\u6b21\u4eb2\u5982\u88ab\u82b1\u53e3\u653e\u513f\u5e38\u6c14\u4e94\u7b2c\u4f7f\u5199\u519b\u5427\u6587\u8fd0\u518d\u679c\u600e\u5b9a\u8bb8\u5feb\u660e\u884c\u56e0\u522b\u98de\u5916\u6811\u7269\u6d3b\u90e8\u95e8\u65e0\u5f80\u8239\u671b\u65b0\u5e26\u961f\u5148\u529b\u5b8c\u5374\u7ad9\u4ee3\u5458\u673a\u66f4\u4e5d\u60a8\u6bcf\u98ce\u7ea7\u8ddf\u7b11\u554a\u5b69\u4e07\u5c11\u76f4\u610f\u591c\u6bd4\u9636\u8fde\u8f66\u91cd\u4fbf\u6597\u9a6c\u54ea\u5316\u592a\u6307\u53d8\u793e\u4f3c\u58eb\u8005\u5e72\u77f3\u6ee1\u65e5\u51b3\u767e\u539f\u62ff\u7fa4\u7a76\u5404\u516d\u672c\u601d\u89e3\u7acb\u6cb3\u6751\u516b\u96be\u65e9\u8bba\u5417\u6839\u5171\u8ba9\u76f8\u7814\u4eca\u5176\u4e66\u5750\u63a5\u5e94\u5173\u4fe1\u89c9\u6b65\u53cd\u5904\u8bb0\u5c06\u5343\u627e\u4e89\u9886\u6216\u5e08\u7ed3\u5757\u8dd1\u8c01\u8349\u8d8a\u5b57\u52a0\u811a\u7d27\u7231\u7b49\u4e60\u9635\u6015\u6708\u9752\u534a\u706b\u6cd5\u9898\u5efa\u8d76\u4f4d\u5531\u6d77\u4e03\u5973\u4efb\u4ef6\u611f\u51c6\u5f20\u56e2\u5c4b\u79bb\u8272\u8138\u7247\u79d1\u5012\u775b\u5229\u4e16\u521a\u4e14\u7531\u9001\u5207\u661f\u5bfc\u665a\u8868\u591f\u6574\u8ba4\u54cd\u96ea\u6d41\u672a\u573a\u8be5\u5e76\u5e95\u6df1\u523b\u5e73\u4f1f\u5fd9\u63d0\u786e\u8fd1\u4eae\u8f7b\u8bb2\u519c\u53e4\u9ed1\u544a\u754c\u62c9\u540d\u5440\u571f\u6e05\u9633\u7167\u529e\u53f2\u6539\u5386\u8f6c\u753b\u9020\u5634\u6b64\u6cbb\u5317\u5fc5\u670d\u96e8\u7a7f\u5185\u8bc6\u9a8c\u4f20\u4e1a\u83dc\u722c\u7761\u5174\u5f62\u91cf\u54b1\u89c2\u82e6\u4f53\u4f17\u901a\u51b2\u5408\u7834\u53cb\u5ea6\u672f\u996d\u516c\u65c1\u623f\u6781\u5357\u67aa\u8bfb\u6c99\u5c81\u7ebf\u91ce\u575a\u7a7a\u6536\u7b97\u81f3\u653f\u57ce\u52b3\u843d\u94b1\u7279\u56f4\u5f1f\u80dc\u6559\u70ed\u5c55\u5305\u6b4c\u7c7b\u6e10\u5f3a\u6570\u4e61\u547c\u6027\u97f3\u7b54\u54e5\u9645\u65e7\u795e\u5ea7\u7ae0\u5e2e\u5566\u53d7\u7cfb\u4ee4\u8df3\u975e\u4f55\u725b\u53d6\u5165\u5cb8\u6562\u6389\u5ffd\u79cd\u88c5\u9876\u6025\u6797\u505c\u606f\u53e5\u533a\u8863\u822c\u62a5\u53f6\u538b\u6162\u53d4\u80cc\u7ec6";
		// 数字和字母的组合
		String baseNumLetter = "0123456789ABCDEFGHJKLMNOPQRSTUVWXYZ";
		// 纯数字
		String baseNum = "0123456789";
		// 纯字母
		String baseLetter = "ABCDEFGHJKLMNOPQRSTUVWXYZ";

		switch(this.type) {
			case CHINESE_CHARACTER : {randomNum = createRandomChar(g, baseChineseChar); break;}
			case ENGLISH_CHARACTER_NUMBER : {randomNum = createRandomChar(g, baseNumLetter); break;}
			case ENGLISH_CHARACTER : {randomNum = createRandomChar(g, baseLetter); break;}
			case NUMBER : {randomNum = createRandomChar(g, baseNum); break;}
			default : {randomNum = createRandomChar(g, baseNumLetter); break;}
		}
		
		return randomNum;
	}

	/**
	 * 创建随机字符
	 * 
	 * @param g
	 * @param baseChar
	 * @return 随机字符
	 */
	private String createRandomChar(Graphics2D g, String baseChar) {
		StringBuffer sb = new StringBuffer();
		int x = 5;
		String ch = "";
		// 控制字数
		for (int i = 0; i < this.length; i++) {
			if(i%4==0){//第一个验证码用黄色
				g.setColor(Color.red);
			}else if(i%4==1){//第二个验证码用绿色
				g.setColor(Color.blue);
			}else if(i%4==2){//第三个验证码用蓝色
				g.setColor(Color.green);
			}else if(i%4==3){//第四个验证码用蓝色
				g.setColor(Color.black);
			}
			// 设置字体旋转角度
			int degree = new Random().nextInt() % 30;
			ch = baseChar.charAt(new Random().nextInt(baseChar.length())) + "";
			sb.append(ch);
			// 正向角度
			g.rotate(degree * Math.PI / 180, x, 20);
			g.drawString(ch, x, 25);
			// 反向角度
			g.rotate(-degree * Math.PI / 180, x, 20);
			x += 23;
		}
		return sb.toString();
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public Color getBackgroudColor() {
		return backgroundColor;
	}

	public void setBackgroudColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
	
}

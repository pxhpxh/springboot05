package com.example.util;

import org.apache.tomcat.util.codec.binary.Base64;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class VerificationCode {
    private static final int width = 140;// 生成验证码图片的宽度
    private static final int height = 47;// 生成验证码图片的高度
    private static final double t = 0.001;//偏移量，越小曲线越精细
    private static final String[] fontNames = { "宋体", "楷体", "隶书", "微软雅黑" };
    //private static final Color bgColor = new Color(255, 255, 255);// 定义验证码图片的背景颜色为白色
    private static final Random random = new Random();
    private static final String codes = "123456789abcdefghjkmnpqrstuvwxyzABCDEFGHJKLMNPQRSTUVWXYZ";
    //private String text;// 记录随机字符串

    /**
     * 获取一个随意颜色
     *
     * @return
     */
    private static Color randomFontColor() {
        int red = random.nextInt(150);
        int green = random.nextInt(150);
        int blue = random.nextInt(150);
        return new Color(red, green, blue);
    }

    //
    private static Color randomBackgroundColor() {
        int red = 200+random.nextInt(50);
        int green = 200+random.nextInt(50);
        int blue = 200+random.nextInt(50);
        return new Color(red, green, blue);
    }

    /**
     * 获取一个随机字体
     *
     * @return
     */
    private static Font randomFont() {
        String name = fontNames[random.nextInt(fontNames.length)];
        int style = random.nextInt(4);
        int size = random.nextInt(5) + 24;
        return new Font(name, style, size);
    }

    /**
     * 获取一个随机字符
     *
     * @return
     */
    private static char randomChar() {
        return codes.charAt(random.nextInt(codes.length()));
    }

    /**
     * 创建一个空白的BufferedImage对象
     *
     * @return
     */
    private static BufferedImage createImage() {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = (Graphics2D) image.getGraphics();
        g2.setColor(randomBackgroundColor());// 设置验证码图片的背景颜色
        g2.fillRect(0, 0, width, height);
        return image;
    }

    public static Map<String, String> getImage() {
        Map<String, String> ret=new HashMap<>();
        BufferedImage image = createImage();
        Graphics2D g2 = (Graphics2D) image.getGraphics();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 4; i++) {
            String s = randomChar() + "";
            sb.append(s);
            g2.setColor(randomFontColor());
            g2.setFont(randomFont());
            int offsetY=random.nextInt(20);
            float x = i * width * 1.0f / 4;
            float y=20+offsetY;
            if(i>0) {
                x += (random.nextInt(20) - 10);
            }
            g2.drawString(s, x, y);
        }
        drawLine(image);
        ByteArrayOutputStream stream = null;
        try {
            stream=new ByteArrayOutputStream();
            ret.put("id", UUIDUtils.generateUUID());
            ret.put("code", sb.toString());
            ImageIO.write(image, "png", stream);
            Base64 base = new Base64();
            String base64 = base.encodeToString(stream.toByteArray());
            ret.put("img", "data:image/png;base64,"+base64);
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if(stream!=null){
                try {
                    stream.close();
                } catch (Exception e){}
            }
        }
        return ret;
    }

    /**
     * 绘制干扰线
     *
     * @param image
     */
    private static void drawLine(BufferedImage image) {
        Graphics2D g2 = (Graphics2D) image.getGraphics();
        for (int i = 0; i < 2; i++) {
            int x1 = random.nextInt(width);
            int y1 = random.nextInt(height);
            int x2 = random.nextInt(width);
            int y2 = random.nextInt(height);
            int x3 = random.nextInt(width);
            int y3 = random.nextInt(height);
            g2.setColor(randomFontColor());
            //g2.setStroke(new BasicStroke(1.5f));
            double x, y;
            for (double k = t; k <= 1 + t; k += t) {
                double r = 1 - k;
                x = Math.pow(r, 2) * x1 + 2 * k * r * x2 + Math.pow(k, 2) * x3;
                y = Math.pow(r, 2) * y1 + 2 * k * r * y2 + Math.pow(k, 2) * y3;
                g2.drawOval((int) x, (int) y, 1, 1);// 画圆的方式比下面注释掉的画线效果更好
            }
        }

        for (int i = 0; i < 2; i++) {
            int x1 = random.nextInt(width);
            int y1 = random.nextInt(height);
            int x2 = random.nextInt(width);
            int y2 = random.nextInt(height);
            g2.setColor(randomFontColor());
            //g2.setStroke(new BasicStroke(1.5f));
            g2.drawLine(x1, y1, x2, y2);
        }
    }

    public static void main(String[] args) {
        Map<String, String> map = getImage();
        for (String key:map.keySet()) {
            System.out.println(key+":"+map.get(key));
        }
    }
}

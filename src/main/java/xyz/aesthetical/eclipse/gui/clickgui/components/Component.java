package xyz.aesthetical.eclipse.gui.clickgui.components;

import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;
import xyz.aesthetical.eclipse.Eclipse;

public class Component {
    protected String title;
    protected double x;
    protected double y;
    protected double width;
    protected double height;

    public Component(String title, double width, double height) {
        this.title = title;
        this.width = width;
        this.height = height;
    }

    public void draw(int mouseX, int mouseY, float tickDelta) {

    }

    public void mouseClicked(int mouseX, int mouseY, int button) {

    }

    public void mouseReleased(int mouseX, int mouseY, int button) {

    }

    public void keyTyped(char character, int code) {

    }

    public float toCenterHeight(float y, float height) {
        return (y + (height / 2.0f)) - (Eclipse.textManager.getHeight() / 2.0f);
    }

    public String getTitle() {
        return title;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public boolean isMouseInBounds(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    public void playClickSound(float pitch) {
        Eclipse.mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, pitch));
    }
}

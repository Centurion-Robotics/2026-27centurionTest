package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.util.ElapsedTime;

public class PIDController {

    private double Kp;
    private double Ki;
    private double Kd;

    private ElapsedTime time;

    private double integral;
    private long lastTime;

    private double lastError;

    private double output;

    public PIDController(double Kp, double Ki, double Kd){
        this.Kp = Kp;
        this.Ki = Kd;
        this.Kd = Kd;
        time = new ElapsedTime();
        lastTime = 0;
    }
    public void update(double error){
        long currentTime = time.nanoseconds();
        double derivative;
        double dt = (currentTime - lastTime)*1e-9;
        integral += error*dt;
        derivative = (error - lastError)/dt;
        output = Kp*dt + Ki*integral + Kd*derivative;

        lastError = error;
        lastTime = currentTime;
    }
    public double getOutput(){
        return output;
    }

    public void reset(){
        lastTime = 0;
        lastError = 0;
        integral = 0;
    }
}

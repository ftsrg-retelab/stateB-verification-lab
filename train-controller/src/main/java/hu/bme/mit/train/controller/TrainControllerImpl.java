package hu.bme.mit.train.controller;
import hu.bme.mit.train.interfaces.TrainController;
public class TrainControllerImpl implements TrainController {
	private int step = 0;
	private int referenceSpeed = 0;
	private int speedLimit = 0;
	private Thread sf;
	public TrainControllerImpl() {
		sf = new SpeedFollower();
		sf.start();
	}
	@Override
	public void followSpeed() {
		if (referenceSpeed < 0) {
			referenceSpeed = 0;
		} else {
			if(referenceSpeed+step > 0) {
				referenceSpeed += step;
			} else {
				referenceSpeed = 0;
			}
		}
		enforceSpeedLimit();
	}
	@Override
	public int getReferenceSpeed() {
		return referenceSpeed;
	}
	@Override
	public void setSpeedLimit(int speedLimit) {
		this.speedLimit = speedLimit;
		enforceSpeedLimit();

	}
	private void enforceSpeedLimit() {
		if (referenceSpeed > speedLimit) {
			referenceSpeed = speedLimit;
		}
	}
	@Override
	public void setJoystickPosition(int joystickPosition) {
		this.step = joystickPosition;
	}
	public class SpeedFollower extends Thread{
		private long lastChecked = 0;
		private long interval = 100;
		@Override
		public void run() {
			lastChecked = System.currentTimeMillis();
			followSpeed();
			try {
				Thread.sleep(interval);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
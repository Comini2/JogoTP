abstract class SimpleConduct{

	protected SimpleConduct(){
		UpdateThread.addConduct(this);
	}

	public abstract void start();

	public abstract void update();
}
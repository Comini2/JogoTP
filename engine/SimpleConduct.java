abstract class SimpleConduct{


	protected SimpleConduct(){
		UpdateThread.addSimpleConduct(this);
	}

	public abstract void start();

	public abstract void update();
}
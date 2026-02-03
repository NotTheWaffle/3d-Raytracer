
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Environment{
	public final List<PhysicalObject> physicalObjects;
	public Environment(){
		physicalObjects = new ArrayList<>();
	}
	public Environment(PhysicalObject... mesh){
		this();
		physicalObjects.addAll(Arrays.asList(mesh));
	}
	public void add(PhysicalObject object){
		physicalObjects.add(object);
	}
}
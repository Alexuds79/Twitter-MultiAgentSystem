package es.usal;
import java.util.List;

public class Checker {
	public boolean isRepeated(String element, List<String> element_list, int element_array_size){
		for(int i = 0; i < element_array_size; i++) if(element.equals(element_list.get(i))) return true;
		return false;
	}
}
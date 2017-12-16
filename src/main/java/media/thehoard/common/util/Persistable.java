/**
 * 
 */
package media.thehoard.common.util;

import java.util.concurrent.Future;

import org.jooq.Field;

/**
 * @author Michael Haas
 *
 */
public interface Persistable {
	public void load();
	public boolean persist();
	public <T> Future<Boolean> persistField(Field<T> field, T value);
	public void delete();
}

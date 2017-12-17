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
	void load();
	boolean persist();
	<T> Future<Boolean> persistField(Field<T> field, T value);
	void delete();
}

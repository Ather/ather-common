/**
 * 
 */
package media.thehoard.common.util.hashes;

/**
 * @author Michael Haas
 *
 */
public enum HashType {
	md5(0);

	private int hashId;

	HashType(int hashId) {
		this.hashId = hashId;
	}

	public int getId() {
		return this.hashId;
	}

	public static HashType getFromId(int id) {
		for (HashType hashType : HashType.values())
			if (hashType.getId() == id)
				return hashType;
		return md5;
	}
}
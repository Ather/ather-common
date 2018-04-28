package com.atherapp.common.ids;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.RandomStringGenerator;

import java.security.SecureRandom;

/**
 * @author Michael Haas
 *
 */
public class Id {
	private String id;

	private Id(IdType idType, int combinationType, int combinationLength) {
		RandomStringGenerator.Builder generatorBuilder = new RandomStringGenerator.Builder();
		generatorBuilder.withinRange('0', 'z');

		switch (combinationType) {
		case CombinationType.AlphaOnly:
			generatorBuilder.filteredBy(Character::isLetter);
			break;
		case CombinationType.NumericOnly:
			generatorBuilder.filteredBy(Character::isDigit);
			break;
		case CombinationType.AdjectiveAdjectiveAnimal:
			SecureRandom random = new SecureRandom();
			String builder = StringUtils
					.capitalize(IdAssets.adjectives[Math.abs(random.nextInt() % IdAssets.adjectives.length)]) +
					StringUtils
							.capitalize(IdAssets.adjectives[Math.abs(random.nextInt() % IdAssets.adjectives.length)]) +
					StringUtils.capitalize(IdAssets.animals[Math.abs(random.nextInt() % IdAssets.animals.length)]);
			// The modulus wraps numbers greater than the array length.
			this.id = builder;
			// XXX This ends the constructor, so consider that for future modifications of
			// this.
			return;
		case CombinationType.AlphaNumeric:
			// AlphaNumeric just jumps to the default case (which is alphanumeric)
		default:
			generatorBuilder.filteredBy(Character::isLetterOrDigit);
			break;
		}

		this.id = generatorBuilder.build().generate(combinationLength);
	}

	public Id(IdType idType) {
		this(idType, determineCombinationType(idType), determineCombinationLength(idType));
	}

	private static int determineCombinationLength(IdType idType2) {
		switch (idType2) {
		case InviteId:
			return 7;
		case AdjectiveAdjectiveAnimal:
		default:
			return -1;
		}
	}

	private static int determineCombinationType(IdType idType2) {
		switch (idType2) {
		case InviteId:
			return CombinationType.AlphaNumeric;
		case AdjectiveAdjectiveAnimal:
		default:
			return CombinationType.AdjectiveAdjectiveAnimal;
		}
	}

	public Id() {
		this(IdType.AdjectiveAdjectiveAnimal, CombinationType.AdjectiveAdjectiveAnimal, -1);
	}

	public String getId() {
		return id;
	}

	@Override
	public String toString() {
		return id;
	}

	@Override
	public boolean equals(Object o) {
		Id compId;
		if (o instanceof Id)
			compId = (Id) o;
		else
			return false;

		return compId.getId().equals(this.id);
	}

	@Override
	public int hashCode() {
		return this.id.hashCode();
	}
}

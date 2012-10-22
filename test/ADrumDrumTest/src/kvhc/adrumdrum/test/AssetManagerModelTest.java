/**
 * aDrumDrum is a step sequencer for Android.
 * Copyright (C) 2012  Daniel Fallstrand, Niclas Ståhl, Oscar Dragén and Viktor Nilsson.
 *
 * This file is part of aDrumDrum.
 *
 * aDrumDrum is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * aDrumDrum is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with aDrumDrum.  If not, see <http://www.gnu.org/licenses/>.
 */
package kvhc.adrumdrum.test;

import junit.framework.Assert;
import android.test.AndroidTestCase;
import kvhc.util.AssetManagerModel;
import kvhc.models.Sound;

/**
 * Test class for kvhc.utils.AssetManagerModel.
 * @author kvhc
 *
 */
public class AssetManagerModelTest extends AndroidTestCase {
	
	/**
	 * Tests getSoundManager()
	 */
	public void testSoundManager() {
		AssetManagerModel<Sound> amm = AssetManagerModel.getSoundManager();
		Assert.assertNotNull(amm);
		
		Sound s = new Sound(1);
		amm.setValue("derp", s);
		
		Assert.assertEquals(s, amm.getValue("derp"));
	}
	
}

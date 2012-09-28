package kvhc.adrumdrum.test;

import junit.framework.Assert;
import kvhc.util.JobQue;
import android.test.AndroidTestCase;

public class JobQueTest extends AndroidTestCase {

	public void testJobQue() {
		JobQue que = new JobQue();
		
		Assert.assertNotNull(que);
	}
}

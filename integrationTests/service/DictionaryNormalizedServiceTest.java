package service;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.trgr.dockets.core.entity.DictionaryNormalized;
import com.trgr.dockets.core.entity.DictionaryNormalizedKey;
import com.trgr.dockets.core.service.DictionaryNormalizedService;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "NotificationServiceIntegrationTest-context.xml" })
@Transactional(transactionManager = "transactionManager")
public class DictionaryNormalizedServiceTest {
	
	@Autowired
	protected DictionaryNormalizedService dictNormService;
	
	@Before
	public void setUp() {

	}
		
	//will have to comeback and write few more services to create the record when running the test instead of using direct value.
	@Test
	public void findNormalizedNamesByCourtAndNamesTest() {
		
		long courtId = 95;
		long typeId =3;
		String originalName = "LAWRENCE N. MARTIN JR. (JHO)";
		
		DictionaryNormalized dictNorm = dictNormService.findNormalizedNamesByPrimaryKey(new DictionaryNormalizedKey(courtId,originalName, typeId));
		assertTrue(dictNorm.getNormalizedName() != null);
		System.out.println(dictNorm.getNormalizedName());

		
		originalName = "AARON E. KLEIN (JHO)";
		
		dictNorm = dictNormService.findNormalizedNamesByPrimaryKey(new DictionaryNormalizedKey(courtId,originalName, typeId));
		assertTrue(dictNorm.getNormalizedName() != null);
		System.out.println(dictNorm.getNormalizedName());
	}

}

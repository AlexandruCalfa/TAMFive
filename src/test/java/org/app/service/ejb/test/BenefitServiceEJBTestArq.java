package org.app.service.ejb.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Random;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ws.rs.core.Response;

import org.app.patterns.EntityManagement;
import org.app.patterns.EntityManagementBase;
import org.app.service.ejb.BenefitServiceEJB;
import org.app.service.ejb.ServiceEJB;
import org.app.service.entities.Benefit;
import org.app.service.entities.EntityBase;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class BenefitServiceEJBTestArq {
	private static Logger logger = Logger.getLogger(BenefitServiceEJBTestArq.class.getName());

	private static int id;

	@EJB
	private static ServiceEJB benefitServiceEJB;

	@BeforeClass
	public static void setup() throws Exception {
		logger.info("DEBUG: Junit testing BenefitServiceEJB ...");
		benefitServiceEJB = new BenefitServiceEJB();
		Random random = new Random();
		id = random.nextInt(20000);
	}

	@AfterClass
	public static void destroy() throws Exception {
		logger.info("DEBUG: Junit end testing BenefitServiceEJB ...");
	}

	public static Archive<?> createDeployment() {
		return ShrinkWrap.create(WebArchive.class, "msd-test.war").addPackage(EntityManagement.class.getPackage())
				.addPackage(ServiceEJB.class.getPackage()).addPackage(EntityBase.class.getPackage())
				.addClass(Benefit.class).addClass(ServiceEJB.class).addClass(BenefitServiceEJB.class)
				.addClass(EntityManagement.class).addClass(EntityManagementBase.class)
				.addAsResource("META-INF/persistance.xml").addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
	}

	@Test
	public void testCreateBenefit() {
		logger.info("Create benefit with id: " + id);
		Response response = benefitServiceEJB.createObject(id + "&salary&2001&20-10-1993&20-10-2555");
		assertNotNull("Benefit creation failed", response);
		assertTrue(response.getStatus() == 200);
	}

	@Test
	public void testReadBenefit() {
		Benefit benefit = (Benefit) benefitServiceEJB.readObject(String.valueOf(id));

		assertNotNull("Benefit creation failed.", benefit);
		assertTrue(benefit.getId().equals(String.valueOf(id)));
	}

	@Test
	public void testUpdateBenefit() {
		Response response = benefitServiceEJB.updateObject(id + "&salary&2002&20-10-1993&20-10-2555");
		assertNotNull("Benefit update failed.", response);
	}

	@Test
	public void testDeleteBenefit() {
		Response response = benefitServiceEJB.deleteObject(String.valueOf(id));
		assertNotNull("Benefit update failed.", response);
	}
}

package org.app.service.ejb;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Random;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.app.patterns.EntityManagementBase;
import org.app.service.entities.Benefit;

@Path("/service/benefit")
@Stateless
@LocalBean
public class BenefitServiceEJB extends EntityManagementBase<Benefit> implements ServiceEJB<Benefit> {
	private static Logger logger = Logger.getLogger(BenefitServiceEJB.class.getName());

	@PersistenceContext(unitName = "MSD")
	private EntityManager entityManager;

	public BenefitServiceEJB() {
	}

	@PostConstruct
	public void init() {
	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/createBenefit/{parameter}")
	@Override
	public Response createObject(@PathParam("parameter") String parameter) {
		Benefit benefit = generateFromParam(parameter);
		try {
			super.createEntity(benefit);
		} catch (PersistenceException e) {
			return Response.status(400)
					.entity("Bad Request. Creating benefit failed. A benefit with the same id already exists.").build();
		} catch (Exception e) {
			logger.info("Creating Benefit failed.");
			logger.info(e.getClass().getName());
		}
		return Response.status(200).entity("Update successful.").build();
	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/readBenefit/{parameter}")
	@Override
	public Benefit readObject(@PathParam("parameter") String parameter) {
		try {
			Benefit benefit = super.readEntity(Benefit.class, parameter);
			logger.info(String.format("Read benefit with fields: <%s> <%s> <%s> <%s> <%s>", benefit.getId(),
					benefit.getType(), benefit.getValue(), benefit.getStartDate(), benefit.getEndDate()));
			return benefit;
		} catch (Exception e) {
			return null;
		}
	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/updateBenefit/{parameter}")
	@Override
	public Response updateObject(@PathParam("parameter") String parameter) {
		Benefit benefit = generateFromParam(parameter);
		try {
			if (!super.updateEntity(benefit)) {
				return Response.status(400)
						.entity(String.format("Bad Request. Entry with id <%s> does not exist.", benefit.getId()))
						.build();
			}
		} catch (Exception e) {
		}
		return Response.status(200).entity("Update successful.").build();
	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/deleteBenefit/{parameter}")
	@Transactional
	@Override
	public Response deleteObject(@PathParam("parameter") String parameter) {
		try {
			if (!super.deleteEntity(Benefit.class, parameter)) {
				return Response.status(400)
						.entity(String.format("There is no entity with id <%s>. Delete operation failed.", parameter))
						.build();
			}
		} catch (Exception e) {
		}
		return Response.status(200).entity(String.format("Benefit with id <%s> was removed.", parameter)).build();
	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/addOneRandomBenefit")
	public Response addRandomObject() {
		Random random = new Random();
		Benefit benefit = new Benefit();
		benefit.setId(Integer.toString(random.nextInt(20000)));
		super.createEntity(benefit);
		return Response.status(200).entity("New Object Added.").build();
	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/ping")
	public Response ping() {
		return Response.status(200).entity("Response 200.").build();
	}

	private Benefit generateFromParam(String parameter) {
		DateFormat startDate = new SimpleDateFormat(Benefit.START_DATE_FORMAT);
		DateFormat endDate = new SimpleDateFormat(Benefit.END_DATE_FORMAT);

		Benefit benefit = new Benefit();
		String[] listOfParameters = parameter.split("&");

		benefit.setId(listOfParameters[0]);
		benefit.setType(listOfParameters[1]);
		benefit.setValue(Double.valueOf(listOfParameters[2]));
		try {
			benefit.setStartDate(startDate.parse(listOfParameters[3]));
			benefit.setEndDate(endDate.parse(listOfParameters[4]));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		logger.info("Benefit instance generated.");
		return benefit;
	}
}
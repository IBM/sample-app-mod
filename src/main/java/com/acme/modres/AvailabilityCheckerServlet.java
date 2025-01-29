package com.acme.modres;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.acme.modres.mbean.IOUtils;
import com.acme.modres.mbean.reservation.DateChecker;
import com.acme.modres.mbean.reservation.ReservationCheckerData;
import com.acme.modres.mbean.reservation.Reservation;

import com.acme.modres.util.ZipValidator;

@WebServlet({ "/resorts/availability" })
public class AvailabilityCheckerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final Logger logger = Logger.getLogger(AvailabilityCheckerServlet.class.getName());

	private static InitialContext context;

	private ReservationCheckerData reservationCheckerData;

	@Override
	public void init() {
		// load reserved dates
		this.reservationCheckerData = new ReservationCheckerData(IOUtils.getReservationListFromConfig());
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		String methodName = "doGet";
		logger.entering(AvailabilityCheckerServlet.class.getName(), methodName);
		int statusCode = 200;

		String selectedDateStr = request.getParameter("date");
		boolean parsedDate = reservationCheckerData.setSelectedDate(selectedDateStr);
		if (!parsedDate || reservationCheckerData.getReservationList() == null) {
			statusCode = 500;
			reservationCheckerData.setAvailablility(false);
		}
		else{
			List<Reservation> reservations = reservationCheckerData.getReservationList().getReservations();
			boolean isAvailible=true;
			for (Reservation reservation : reservations) {
				try {
					Date fromDate = new SimpleDateFormat(Constants.DATA_FORMAT).parse(reservation.getFromDate());
					Date toDate = new SimpleDateFormat(Constants.DATA_FORMAT).parse(reservation.getToDate());
					Date selectedDate = reservationCheckerData.getSelectedDate();
	
					if (selectedDate.after(fromDate) && selectedDate.before(toDate)) {
						isAvailible = false;
						break;
					}
				} catch (ParseException ex) {
					ex.printStackTrace();
				}
			}
	
			reservationCheckerData.setAvailablility(isAvailible);
	
			// Adjust the status code based on availability
			if (!isAvailible) {
				statusCode = 201;
			}
		}
	
		// Send the response
		PrintWriter out = response.getWriter();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		out.print("{\"availability\": \"" + String.valueOf(reservationCheckerData.isAvailible()) + "\"}");
		response.setStatus(statusCode);
	}


	/**
	 * Returns the weather information for a given city
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

	protected int exportRevervations(String selectedDateStr) {
		File fileToZip = IOUtils.getFileFromRelativePath("reservations.json");
		String userDirectory = System.getProperty("user.home");
		String zipPath = userDirectory + "/reservations.zip";
		
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(zipPath);
			ZipOutputStream zipOut = new ZipOutputStream(fos);

			FileInputStream fis = new FileInputStream(fileToZip);
			ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
			zipOut.putNextEntry(zipEntry);

			byte[] bytes = new byte[1024];
			int length;
			while((length = fis.read(bytes)) >= 0) {
				zipOut.write(bytes, 0, length);
			}
			fis.close();

			zipOut.close();
			fos.close();

			// verify zip
			ZipValidator zipValidator = new ZipValidator(new File(zipPath));
			if(zipValidator.isValid()) {
				return 0;
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}

}
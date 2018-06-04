package com.iceps.spring.shardingjdbc1;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ImportResource;

import com.iceps.spring.shardingjdbc1.model.Cust;
import com.iceps.spring.shardingjdbc1.model.Detail;
import com.iceps.spring.shardingjdbc1.model.Order;
import com.iceps.spring.shardingjdbc1.model.Product;
import com.iceps.spring.shardingjdbc1.model.User;
import com.iceps.spring.shardingjdbc1.service.CustService;
import com.iceps.spring.shardingjdbc1.service.DetailService;
import com.iceps.spring.shardingjdbc1.service.OrderService;
import com.iceps.spring.shardingjdbc1.service.ProductService;
import com.iceps.spring.shardingjdbc1.service.UserService;

@SpringBootApplication
@EnableCaching
@ImportResource({ "classpath*:applicationContext.xml" })
public class Application {
	private static ApplicationContext applicationContext;

	public static void main(String[] args) {
		applicationContext = SpringApplication.run(Application.class, args);
		// testAddUser();
		// testFindUser();
		// testAddCust();
		// testFindCust();
		// testSale();
//		testFindOrders();
		testFindDetails();
	}

	public static void testAddUser() {
		UserService userService = applicationContext.getBean(UserService.class);

		long baseId = System.currentTimeMillis() % 1000000;
		Random r = new Random(baseId);
		for (int i = 0; i < 100; i++) {
			User u = new User();
			u.setUserId((int) (baseId + r.nextInt(1000000)));
			u.setUserName(u.getUserId() + "-name");
			u.setPassword("888888");
			u.setPhone(r.nextInt(9) + "");
			System.out.println("add user:" + u);
			userService.addUser(u);
		}
	}

	public static void testFindUser() {
		UserService userService = applicationContext.getBean(UserService.class);
		List<User> users = userService.findAll(1, 10);
		System.out.println("userService.findAllUser.size=" + users.size());
		System.out.println("userService.findAllUser.users.class=" + users.getClass());
		for (User u : users)
			System.out.println("userService.findAllUser.u=" + u);
	}

	public static void testAddCust() {
		CustService custService = applicationContext.getBean(CustService.class);

		long baseId = System.currentTimeMillis() % 1000000;
		Random r = new Random(baseId);
		for (int i = 0; i < 100; i++) {
			Cust o = new Cust();
			o.setCustId((int) (baseId + r.nextInt(1000000)));
			o.setCustName(o.getCustId() + "-name");
			System.out.println("add cust:" + o);
			custService.addCust(o);
		}
	}

	public static void testFindCust() {

		CustService custService = applicationContext.getBean(CustService.class);
		List<Cust> custs = custService.findAll(1, 10);
		System.out.println("custService.findAll.size=" + custs.size());
		for (Cust o : custs)
			System.out.println("userService.findAll.record=" + o);
	}

	public static void testFindOrders() {
		OrderService orderService = applicationContext.getBean(OrderService.class);
		List<Order> rows = orderService.findAll();
		System.out.println("orderService.findAll.size=" + rows.size());
		for (Order o : rows)
			System.out.println("orderService.findAll.record=" + o);

		rows = orderService.selectAllOr();
		System.out.println("orderService.selectAllOr.size=" + rows.size());
		for (Order o : rows)
			System.out.println("orderService.selectAllOr.record=" + o);

		rows = orderService.selectAllOrderBy();
		System.out.println("orderService.selectAllOrderBy.size=" + rows.size());
		for (Order o : rows)
			System.out.println("orderService.selectAllOrderBy.record=" + o);

		rows = orderService.selectAllLike();
		System.out.println("orderService.selectAllLike.size=" + rows.size());
		for (Order o : rows)
			System.out.println("orderService.selectAllLike.record=" + o);

		rows = orderService.selectAllIn();
		System.out.println("orderService.selectAllIn.size=" + rows.size());
		for (Order o : rows)
			System.out.println("orderService.selectAllIn.record=" + o);

		rows = orderService.selectAllRange();
		System.out.println("orderService.selectAllRange.size=" + rows.size());
		for (Order o : rows)
			System.out.println("orderService.selectAllRange.record=" + o);

		rows = orderService.selectDistinct();
		System.out.println("orderService.selectDistinct.size=" + rows.size());
		for (Order o : rows)
			System.out.println("orderService.selectDistinct.record=" + o);

		rows = orderService.selectUnion();
		System.out.println("orderService.selectUnion.size=" + rows.size());
		for (Order o : rows)
			System.out.println("orderService.selectUnion.record=" + o);

		rows = orderService.selectByUserId(2);
		System.out.println("orderService.selectByUserId.size=" + rows.size());
		for (Order o : rows)
			System.out.println("orderService.selectByUserId.record=" + o);
		
	}

	public static void testFindDetails() {
		DetailService detailService = applicationContext.getBean(DetailService.class);
		List<Detail> rows = detailService.selectAll();
		System.out.println("detailService.selectAll.size=" + rows.size());
		for (Detail o : rows)
			System.out.println("detailService.selectAll.record=" + o);

		rows = detailService.selectAllOr();
		System.out.println("detailService.selectAllOr.size=" + rows.size());
		for (Detail o : rows)
			System.out.println("detailService.selectAllOr.record=" + o);

		rows = detailService.selectAllOrderBy();
		System.out.println("detailService.selectAllOrderBy.size=" + rows.size());
		for (Detail o : rows)
			System.out.println("detailService.selectAllOrderBy.record=" + o);

		rows = detailService.selectAllLike();
		System.out.println("detailService.selectAllLike.size=" + rows.size());
		for (Detail o : rows)
			System.out.println("detailService.selectAllLike.record=" + o);

		rows = detailService.selectAllIn();
		System.out.println("detailService.selectAllIn.size=" + rows.size());
		for (Detail o : rows)
			System.out.println("detailService.selectAllIn.record=" + o);

		rows = detailService.selectAllRange();
		System.out.println("detailService.selectAllRange.size=" + rows.size());
		for (Detail o : rows)
			System.out.println("detailService.selectAllRange.record=" + o);

		rows = detailService.selectDistinct();
		System.out.println("detailService.selectDistinct.size=" + rows.size());
		for (Detail o : rows)
			System.out.println("detailService.selectDistinct.record=" + o);

		rows = detailService.selectUnion();
		System.out.println("detailService.selectUnion.size=" + rows.size());
		for (Detail o : rows)
			System.out.println("detailService.selectUnion.record=" + o);

		/*
		 * Random r = new Random(System.currentTimeMillis()); Integer id = null;
		 * 
		 * rows = detailService.selectAll(2, 10);
		 * System.out.println("detailService.selectAllpage2.size=" +
		 * rows.size()); for (Detail o : rows)
		 * System.out.println("detailService.selectAllpage2.record=" + o);
		 * 
		 * rows = detailService.selectAll(3, 10);
		 * System.out.println("detailService.selectAllpage3.size=" +
		 * rows.size()); for (Detail o : rows)
		 * System.out.println("detailService.selectAllpage3.record=" + o);
		 * 
		 * rows = detailService.selectAll(4, 10);
		 * System.out.println("detailService.selectAllpage4.size=" +
		 * rows.size()); for (Detail o : rows)
		 * System.out.println("detailService.selectAllpage4.record=" + o);
		 * 
		 * id = r.nextInt(4) + 1; rows = detailService.selectByCustId(id);
		 * System.out.println("detailService.selectByCustId.size=" + rows.size()
		 * + ", id=" + id); for (Detail o : rows)
		 * System.out.println("detailService.selectByCustId.record=" + o);
		 * 
		 * id = r.nextInt(4) + 1; rows = detailService.selectByProdId(id);
		 * System.out.println("detailService.selectByProdId.size=" + rows.size()
		 * + ", id=" + id); for (Detail o : rows) {
		 * System.out.println("detailService.selectByProdId.record=" + o); }
		 * detailService.updateOrderByProdId(id, "product-" + id); rows =
		 * detailService.selectByProdId(id);
		 * System.out.println("detailService.selectByProdId.size=" + rows.size()
		 * + ", id=" + id); for (Detail o : rows) {
		 * System.out.println("detailService.selectByProdId.record=" + o); }
		 * 
		 * id = r.nextInt(4) + 1; rows = detailService.selectByUserId(id);
		 * System.out.println("detailService.selectByUserId.size=" + rows.size()
		 * + ", id=" + id); for (Detail o : rows)
		 * System.out.println("detailService.selectByUserId.record=" + o);
		 */
	}

	public static void testSale() {
		UserService userService = applicationContext.getBean(UserService.class);
		CustService custService = applicationContext.getBean(CustService.class);
		ProductService productService = applicationContext.getBean(ProductService.class);
		OrderService orderService = applicationContext.getBean(OrderService.class);

		long baseId = System.currentTimeMillis() % 1000000;
		Random r = new Random(baseId);
		for (int i = 0; i < 10; i++) {
			Cust cust = new Cust();
			cust.setCustId((int) (baseId + r.nextInt(1000000)));
			cust.setCustName("cust-" + cust.getCustId() + "-name");
			System.out.println("add cust:" + cust);
			custService.addCust(cust);

			for (int j = 0; j < 10; j++) {
				String orderId = cust.getCustId().toString();
				orderId = (baseId + r.nextInt(1000000)) + orderId.substring(orderId.length() - 2, orderId.length());
				Order order = new Order();
				order.setOrderId(orderId);
				order.setCustId(cust.getCustId());
				order.setProdId(r.nextInt(4) + 1);
				order.setUserId(r.nextInt(3) + 1);
				order.setAmount((new BigDecimal(r.nextDouble())).multiply(new BigDecimal(1000)));
				order.setAmount(order.getAmount().setScale(2, RoundingMode.HALF_UP));
				order.setRemark((new Date()).toString());

				Product product = productService.selProduct(order.getProdId());
				User user = userService.selUser(order.getUserId());
				System.out.println("add order:" + order);
				System.out.println("    product:" + product);
				System.out.println("    user:" + user);

				orderService.addOrder(order);

			}

		}
	}

}

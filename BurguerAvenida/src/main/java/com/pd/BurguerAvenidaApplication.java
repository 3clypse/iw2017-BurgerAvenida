package com.pd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;

import com.pd.dao.security.RoleDao;
import com.pd.dao.security.UserDao;
import com.pd.service.RestaurantService;
import com.pd.service.ZoneService;
import com.pd.service.security.RoleService;
import com.pd.service.security.UserService;

@SpringBootApplication
public class BurguerAvenidaApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(BurguerAvenidaApplication.class, args);
	}

	@Autowired
	RoleService roleService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	RestaurantService restaurantService;
	
	@Autowired
	ZoneService zoneService;
	
	@Autowired
	UserDao userDao;
	
	@Autowired
	RoleDao roleDao;
	
	@Override
	@Transactional
	public void run(String... args) throws Exception {
		//Data first load
		
		//Roles
		/*roleService.create(RoleName.ROLE_WAITER);
		Role waiter = roleDao.findByName(RoleName.ROLE_WAITER);
		roleService.create(RoleName.ROLE_ATTENDANT);
		Role attendant = roleDao.findByName(RoleName.ROLE_ATTENDANT);
		roleService.create(RoleName.ROLE_MANAGER);
		Role manager = roleDao.findByName(RoleName.ROLE_MANAGER);
		
		//Restaurants
		restaurantService.create("Avenida Burguer nº1", "Avenida Andalucia s/n", userService.read("lionel"));
		restaurantService.create("Avenida Burguer nº2", "Calle Jerez nº 14", userService.read("cristiano"));
		
		//Manager
		userService.save(
				new User("admin", "admin", "Admin", "Admin", "admin@admin.com", null, new HashSet<>(Lists.newArrayList(manager))));
		
		//Attendants
		userService.save(
				new User("lionel", "lionel", "Leo Andres", "Messi", "leo@messi.com", null, new HashSet<>(Lists.newArrayList(attendant, waiter))));
		userService.save(
				new User("cristiano", "cristiano", "Cristiano", "Ronaldo", "cristiano@ronaldo.com", null, new HashSet<>(Lists.newArrayList(attendant, waiter))));
		
		User cristiano = userService.read("cristiano");
		cristiano.setRoles(roleService.findAll());
		userDao.save(cristiano);
		
		
		//Zones
		Restaurant aB1 = restaurantService.findByName("Avenida Burguer nº1");
		Restaurant aB2 = restaurantService.findByName("Avenida Burguer nº2");
		
		//Bars
		zoneService.create(ZoneType.BAR, "Barra 1", aB1);
		zoneService.create(ZoneType.BAR, "Barra 2", aB1);
		zoneService.create(ZoneType.BAR, "Barra 3", aB1);
		zoneService.create(ZoneType.BAR, "Barra 4", aB1);
		zoneService.create(ZoneType.BAR, "Barra 1", aB2);
		zoneService.create(ZoneType.BAR, "Barra 2", aB2);
		zoneService.create(ZoneType.BAR, "Barra 3", aB2);
		zoneService.create(ZoneType.BAR, "Barra 4", aB2);
		//Lounge
		zoneService.create(ZoneType.LOUNGE, "Mesa salon 1", aB1);
		zoneService.create(ZoneType.LOUNGE, "Mesa salon 2", aB1);
		zoneService.create(ZoneType.LOUNGE, "Mesa salon 3", aB1);
		zoneService.create(ZoneType.LOUNGE, "Mesa salon 4", aB1);
		zoneService.create(ZoneType.LOUNGE, "Mesa salon 5", aB1);
		zoneService.create(ZoneType.LOUNGE, "Mesa salon 6", aB1);
		zoneService.create(ZoneType.LOUNGE, "Mesa salon 7", aB1);
		zoneService.create(ZoneType.LOUNGE, "Mesa salon 8", aB1);
		zoneService.create(ZoneType.LOUNGE, "Mesa salon 1", aB2);
		zoneService.create(ZoneType.LOUNGE, "Mesa salon 2", aB2);
		zoneService.create(ZoneType.LOUNGE, "Mesa salon 3", aB2);
		zoneService.create(ZoneType.LOUNGE, "Mesa salon 4", aB2);
		zoneService.create(ZoneType.LOUNGE, "Mesa salon 5", aB2);
		zoneService.create(ZoneType.LOUNGE, "Mesa salon 6", aB2);
		zoneService.create(ZoneType.LOUNGE, "Mesa salon 7", aB2);
		zoneService.create(ZoneType.LOUNGE, "Mesa salon 8", aB2);
		//Terrace
		zoneService.create(ZoneType.TERRACE, "Mesa terraza 1", aB1);
		zoneService.create(ZoneType.TERRACE, "Mesa terraza 2", aB1);
		zoneService.create(ZoneType.TERRACE, "Mesa terraza 3", aB1);
		zoneService.create(ZoneType.TERRACE, "Mesa terraza 4", aB1);
		zoneService.create(ZoneType.TERRACE, "Mesa terraza 1", aB2);
		zoneService.create(ZoneType.TERRACE, "Mesa terraza 2", aB2);
		zoneService.create(ZoneType.TERRACE, "Mesa terraza 3", aB2);
		zoneService.create(ZoneType.TERRACE, "Mesa terraza 4", aB2);*/
		
		
	}
}

package com.pd;

import java.util.Arrays;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.pd.dto.UserPostDto;
import com.pd.model.Restaurant;
import com.pd.model.ZoneType;
import com.pd.model.security.RoleName;
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
	
	@Override
	public void run(String... args) throws Exception {
		//Data first load
		
		//Roles
		roleService.create(RoleName.ROLE_WAITER);
		roleService.create(RoleName.ROLE_ATTENDANT);
		roleService.create(RoleName.ROLE_MANAGER);
		
		//Manager
		userService.create(
				new UserPostDto("admin", "admin", "Admin", "Admin", "admin@admin.com"), 
				new HashSet<RoleName>(Arrays.asList(RoleName.ROLE_MANAGER)));
		
		//Attendants
		userService.create(
				new UserPostDto("lionel", "lionel", "Leo Andres", "Messi", "leo@messi.com"),
				new HashSet<RoleName>(Arrays.asList(RoleName.ROLE_ATTENDANT, RoleName.ROLE_WAITER)));
		userService.create(
				new UserPostDto("cristiano", "cristiano", "Cristiano", "Ronaldo", "cristiano@ronaldo.com"),
				new HashSet<RoleName>(Arrays.asList(RoleName.ROLE_ATTENDANT, RoleName.ROLE_WAITER)));
		
		//Restaurants
		restaurantService.create("Avenida Burguer nº1", "Avenida Andalucia s/n", userService.read("lionel"));
		restaurantService.create("Avenida Burguer nº2", "Calle Jerez nº 14", userService.read("cristiano"));
		
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
		zoneService.create(ZoneType.TERRACE, "Mesa terraza 4", aB2);
		
		
	}
}

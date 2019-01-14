package com.instana.test.badsql;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

@SpringBootApplication
public class BadSqlTestApplication {

	private final static Logger LOGGER = LoggerFactory.getLogger(BadSqlTestApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(BadSqlTestApplication.class, args);
	}

	@RestController
	@RequestMapping(path = "/api/greetings")
	static class ApiController {

		private final JdbcTemplate jdbcTemplate;

		ApiController(JdbcTemplate jdbcTemplate) {
			this.jdbcTemplate = jdbcTemplate;
		}

		@GetMapping(produces = TEXT_PLAIN_VALUE)
		String sayHello() {
			StringBuffer init = new StringBuffer("CREATE TABLE IF NOT EXISTS MyTable (");

			for (int i = 1; i < 21; i++) {
				init.append(String.format("column_%02d VARCHAR(255)", i));

				if (i != 20) {
					init.append(", ");
				}
			}

			init.append(")");

			jdbcTemplate.execute(init.toString());

			StringBuffer sb = new StringBuffer("INSERT INTO MyTable (");
			for (int i = 1; i < 21; i++) {
				sb.append(String.format("column_%02d", i));

				if (i != 20) {
					sb.append(", ");
				}
			}

			sb.append(") VALUES (");

			for (int i = 1; i < 21; i++) {
				sb.append('\'');

				sb.append(RandomStringUtils.randomAlphanumeric(255));

				sb.append('\'');

				if (i != 20) {
					sb.append(", ");
				}
			}

			sb.append(")");

			LOGGER.info("Running a query {} characters long", sb.length());

			jdbcTemplate.execute(sb.toString());

			return "Hello!";
		}

	}

}
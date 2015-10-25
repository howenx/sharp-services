package domain;

import play.data.validation.Constraints;
import play.data.validation.ValidationError;

import java.util.ArrayList;
import java.util.List;

/**
 * validation test
 * Created by hao on 15/10/25 21:00.
 */
public class Vuser {
		@Constraints.Required
		public String name;

		@Constraints.Required
		@Constraints.Min(0)
		public Integer age;

		public List<ValidationError> validate() {
				List<ValidationError> errors = new ArrayList<ValidationError>();
				if (getName().length() >10 ) {
						errors.add(new ValidationError("name", "This e-mail is already registered."));
				}
				return errors.isEmpty() ? null : errors;
		}


		public Vuser(String name, Integer age) {
				this.name = name;
				this.age = age;
		}

		public String getName() {
				return name;
		}

		public void setName(String name) {
				this.name = name;
		}

		public Integer getAge() {
				return age;
		}

		public void setAge(Integer age) {
				this.age = age;
		}
}

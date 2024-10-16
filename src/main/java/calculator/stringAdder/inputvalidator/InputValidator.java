package calculator.stringAdder.inputvalidator;

import camp.nextstep.edu.missionutils.Console;

/**
 * @author : jiffyin7@gmail.com
 * @since : 24. 10. 16.
 */
public class InputValidator {

  public String validateInput() {
    System.out.println("덧셈할 문자열을 입력해주세요.");
    String input = Console.readLine();
    if (input == null || input.isEmpty()){
      throw new IllegalArgumentException(this.getClass().getSimpleName()+ ": 입력한 문자열이 비었습니다.");
    }
    return input;
  }
}

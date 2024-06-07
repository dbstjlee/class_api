package ch01;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class APIExplorer {

	public static void main(String[] args) throws IOException {

		// 순수 자바코드로 (클라이언트 측 코딩)
		// 준비물
		// 1. 서버측 주소 - 경로
		// 단일 스레딩이면 StringBuilder
		// 멀티 스레딩이면 StringBuffer
		// http://localhost:8080/test?name=홍길동&age=20
		// -> 'GET' 요청은 HTTP 바디가 없기 때문에 end point에 ?해서 값을 던짐.
		// http://localhost:8080/test?name=%ED%99%8D%EA%B8%B8%EB%8F%99&age=20
		// --> url 인코딩으로 바뀐거임.
		StringBuilder urlBuilder = new StringBuilder(
				"http://apis.data.go.kr/B552584/UlfptcaAlarmInqireSvc/getUlfptcaAlarmInfo"); // 코드 복붙
		urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") // 발급받은 개인 API 인증키 입력
				+ "=Jr9nwBsnEI5J9uV%2FbS21LO5BqzxuECqUfyO5j5g4oIp%2BhIJ1RtAAk%2FWFQ6k48sFCzUz8SeTbKXU0gdBAh0ELUg%3D%3D");
		// UTF-8형식으로 감싸라.
		urlBuilder.append("&" + URLEncoder.encode("returnType", "UTF-8") + "=" + URLEncoder.encode("xml", "UTF-8")); // 마임타임?
																														// 확장?
		urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("100", "UTF-8"));
		urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8"));
		urlBuilder.append("&" + URLEncoder.encode("year", "UTF-8") + "=" + URLEncoder.encode("2024", "UTF-8"));
		// 여기에 연도만 바꾸면 실행 시 연도 바껴서 출력됨.
		urlBuilder.append("&" + URLEncoder.encode("itemCode", "UTF-8") + "=" + URLEncoder.encode("PM10", "UTF-8"));

		// URL 객체에 문자열 경로 넣어서 객체 생성
		// url.openConnection() 데이터 요청 보내기 - 설정하고

		URL url = new URL(urlBuilder.toString());
		// URL 객체 생성
		// URL이 String 타입이라 toString()으로 String 타입으로 변경

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		// .openConnection() + 다운 캐스팅

		conn.setRequestMethod("GET"); // 서버에 있는 자원 요청
		conn.setRequestProperty("Content-type", "application/json");
		// 성공 200, 실패 404, 405
		System.out.println("Response code: " + conn.getResponseCode()); // 코드를 받아서 확인함.
		// 200 출력됨.(성공)

		// 100 ~ 500 까지는 의미가 있다(약속) // 200 ~ 300은 성공
		BufferedReader rd;
		if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
			rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		} else {
			rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
			// 실패 코드 404 출력
		}
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = rd.readLine()) != null) {
			sb.append(line);
		}
		rd.close();
		conn.disconnect();
		System.out.println(sb.toString());
	}// end of main
}// end of class

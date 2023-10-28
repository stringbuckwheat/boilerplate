import axios from "axios";

// 인증이 필요한 API에서 사용할 axios 객체
const authAxios = axios.create({
    baseUrl: `${process.env.REACT_APP_API}`,
    headers: {
        Authorization: `Bearer ${localStorage.getItem("accessToken")}`
    }
})

// interceptor를 사용해서 요청마다 header에 accessToken을 넣어줍니다.
authAxios.interceptors.request.use(
    (config) => {
        const accessToken = localStorage.getItem("accessToken");

        if(accessToken){
            config.headers.Authorization = `Bearer ${accessToken}`;
        }

        return config;
    },
    (error) => console.log("request interceptor error", error)
)

// 예외 처리 및 재 로그인 유도 로직
authAxios.interceptors.response.use(
    (response) => response,
    (error) => {
        // 로그아웃 시켜야 할 에러코드
        if(error.response.data.errorCode === "B001"){
            alert(error.response.data.msg);
            localStorage.clear();
            window.location.replace('/');
        }

        return Promise.reject(error.response.data);
    }
)

export default authAxios;
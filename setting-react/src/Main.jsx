import Login from "./Login";
import {useNavigate} from "react-router-dom";
import authAxios from "./interceptors";
import {useEffect, useState} from "react";

const Main = () => {
    const [authData, setAuthData] = useState("");
    const navigate = useNavigate();

    // auth 필요한 API 테스트
    const componentDidMount = async () => {
        const res = (await authAxios.get("/auth/test")).data;
        setAuthData(() => res.authTest);
    }

    useEffect(() => {componentDidMount()}, []);

    const logout = () => {
        localStorage.clear();
        navigate("/");
    }

    return (
        <>
            <h1>메인 페이지</h1>
            <div>인증이 필요한 데이터</div>
            <div>authTest: {authData}</div>
            <button onClick={logout}>로그아웃</button>
        </>
    )
}

export default Main;
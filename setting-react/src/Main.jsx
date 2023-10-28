import Login from "./Login";
import {useNavigate} from "react-router-dom";
import authAxios from "./interceptors";
import {useEffect, useState} from "react";

const Main = () => {
    const [loginUser, setLoginUser] = useState("");
    const navigate = useNavigate();

    // auth 필요한 API 테스트
    const componentDidMount = async () => {
        const res = (await authAxios.get("/auth/test")).data;
        setLoginUser(() => res.authTest);
    }

    useEffect(() => {componentDidMount()}, []);

    const logout = () => {
        localStorage.clear();
        navigate("/");
    }

    return (
        <>
            <h1>메인 페이지</h1>
            <div>로그인 유저 정보</div>
            <div>userPk: {loginUser.userId}</div>
            <div>username: {loginUser.username}</div>
            <div>name: {loginUser.name}</div>
            <br />
            <button onClick={logout}>로그아웃</button>
        </>
    )
}

export default Main;
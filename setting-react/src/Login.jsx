import {useState} from "react";
import axios from "axios";
import {useNavigate} from "react-router-dom";

const Login = () => {
    const [login, setLogin] = useState({username: 'memil', password: 'loveyoumemil'});
    const [loginMsg, setLoginMsg] = useState("");
    const navigate = useNavigate();
    const onChange = (e) => {
        const {name, value} = e.target;
        setLogin({...login, [name]: value});
    }

    const submit = async (e) => {
        e.preventDefault();

        try{
            const res = await axios.post(`${process.env.REACT_APP_API}/login`, login);

            console.log("res", res.data);
            localStorage.setItem("accessToken", res.data.accessToken); // access token 저장
            setLoginMsg(res.data.message);

            // 메인 페이지로 이동
            navigate("/main", {replace: true});
        } catch (e){
            setLoginMsg(() => "로그인 실패");
        }
    }

    return (
        <>
            <form onSubmit={(e) => submit(e)}>
                <div>
                    <label>username: </label>
                    <input name={"username"} defaultValue={"memil"} onChange={onChange}/>
                </div>
                <div>
                    <label>password: </label>
                    <input name={"password"} defaultValue={"loveyoumemil"} onChange={onChange}/>
                </div>
                <br/>
                <button type={"submit"}>로그인</button>
            </form>
            <div>{loginMsg}</div>
        </>
    )
}

export default Login;
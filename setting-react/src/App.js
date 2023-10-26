import {useState} from "react";
import axios from "axios";

function App() {
    const [login, setLogin] = useState({username: 'memil', password: 'loveyoumemil'});
    const [loginMsg, setLoginMsg] = useState("");
    const onChange = (e) => {
        const {name, value} = e.target;
        setLogin({...login, [name]: value});
    }

    const submit = async (e) => {
        e.preventDefault();

        // MEMIL process.env.REACT_APP_API => env 파일 정보 읽어오기
        const res = await axios.post(`${process.env.REACT_APP_API}/login`, login)
            .catch((error) => setLoginMsg(() => "로그인 실패"));

        if(res){
            setLoginMsg(res.data.message);
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
    );
}

export default App;

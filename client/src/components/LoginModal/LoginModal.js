import styled from 'styled-components';
import userStore from '../../store/userStore';
import { CloseOutlined } from '@ant-design/icons';
import { useState } from 'react';
import Signup from '../Signup/Signup';

const backendUrl = 'http://13.125.30.88/';

function LoginModal(props) {
  const setUserId = userStore((state) => state.setUserId);

  const [signUp, setSignUp] = useState(false);
  const [id, setId] = useState('');
  const [password, setPassword] = useState('');
  const googleLogin = () => {};

  const closeButton = () => {
    // eslint-disable-next-line react/prop-types
    props.onClose();
  };

  const onChangeId = (e) => {
    setId(e);
  };

  const onChangePassword = (e) => {
    setPassword(e);
  };

  const SignIn = async () => {
    // const res = await axios.get(
    //   `${backendUrl}auth/login`,
    //   {
    //     email: id,
    //     password: password,
    //   },
    //   { withCredentials: true }
    // );
    const user_id = 5;
    setUserId(user_id);
    // axios sign in
  };

  const goSignUp = () => {
    setSignUp(true);
    console.log(signUp);
  };

  return (
    <Overlay>
      <ModalWrap>
        <CloseOutlined
          style={{ 'margin-left': '85%', 'margin-top': '5%' }}
          onClick={closeButton}
        />
        <Contents>
          <h1
            style={{
              'margin-bottom': '5%',
              display: 'flex',
              'justify-content': 'space-evenly',
              fontSize: '30px',
              position: 'relative',
              top: '-30px',
            }}
          >
            Look at me
          </h1>
          <h2 style={{ fontSize: '20px', position: 'relative', top: '-30px' }}>
            로그인
          </h2>
          <input
            className="id"
            placeholder="아이디"
            style={{
              'font-size': '14px',
              padding: '12px 12px',
              'background-color': 'gary',
              'border-radius': '0px',
              width: '250px',
              color: 'black',
              'font-weight': '200',
              'margin-top': '-30px',
              position: 'relative',
              top: '-20px',
            }}
            onChange={onChangeId}
          ></input>
          <input
            className="pw"
            placeholder="비밀번호"
            style={{
              'font-size': '14px',
              padding: '12px 12px',
              width: '250px',
              'background-color': 'gary',
              'border-radius': '0px',
              color: 'black',
              'font-weight': '200',
              'margin-top': '-30px',
              position: 'relative',
              top: '0px',
            }}
            onChange={onChangePassword}
          ></input>
          <SigninButton onClick={SignIn}>로그인</SigninButton>
          <div
            style={{
              display: 'flex',
              'align-items': 'center',
              'margin-top': '15px',
              'justify-content': 'center',
            }}
          >
            <div>아직 회원이 아니신가요?</div>
            <SignupButton onClick={goSignUp}>회원 가입</SignupButton>
          </div>
          <div> --------------------- OR ---------------------</div>
          <GoogleButton onClick={googleLogin}>구글 로그인</GoogleButton>
        </Contents>
      </ModalWrap>
      {signUp && (
        <Signup
          open={signUp}
          onClose={() => {
            setSignUp(false);
          }}
        />
      )}
    </Overlay>
  );
}

const Overlay = styled.div`
  position: fixed;
  width: 100%;
  height: 100%;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
  background: rgba(0, 0, 0, 0.2);
  z-index: 9999;
  text-align: center;
`;

const ModalWrap = styled.div`
  width: 400px;
  height: 500px;
  border-radius: 15px;
  background-color: #fff;
  position: absolute;
  top: 40%;
  left: 50%;
  transform: translate(-50%, -50%);
`;

const Contents = styled.div`
  margin: 50px 30px;
  h1 {
    font-size: 30px;
    font-weight: 600;
    margin-bottom: 60px;
  }
  img {
    margin-top: 60px;
    width: 300px;
  }
`;

const SigninButton = styled.button`
  font-size: 14px;
  padding: 12px 10px;
  background-color: #ababab;
  border-radius: 0px;
  color: white;
  font-style: italic;
  font-weight: 200;
  margin-top: 10px;
  cursor: pointer;
  &:hover {
    background-color: #898989;
  }
`;

const SignupButton = styled.button`
  font-size: 14px;
  border: none;
  color: black;
  font-style: italic;
  font-weight: 200;
  cursor: pointer;
`;

const GoogleButton = styled.button`
  font-size: 14px;
  padding: 12px 120px;
  background-color: #ababab;
  border-radius: 0px;
  color: white;
  font-style: italic;
  font-weight: 200;
  margin-top: 10px;
  cursor: pointer;
  &:hover {
    background-color: #898989;
  }
`;

export default LoginModal;

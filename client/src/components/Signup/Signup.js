import styled from 'styled-components';
import axios from 'axios';
import { CloseOutlined } from '@ant-design/icons';
import { useState } from 'react';

const backendUrl = 'http://13.125.30.88/';

function Signup(props) {
  const [id, setId] = useState('');
  const [password, setPassword] = useState('');

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

  const SignUpUser = async () => {
    const res = await axios.post(
      `${backendUrl}auth/signup`,
      {
        nickname: 'test',
        email: 'test@test.com',
        password: 'test123',
        height: 180,
        weight: 70,
      },
      { withCredentials: true }
    );
    console.log(res.data);
    // axios sign in
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
            회원가입
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
              top: '-30px',
              height: '5px',
            }}
            onChange={onChangeId}
          ></input>
          <input
            className="pw"
            placeholder="비밀번호"
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
              top: '-23px',
              height: '5px',
            }}
            onChange={onChangePassword}
          ></input>
          <input
            className="vfpw"
            placeholder="비밀번호확인"
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
              top: '-18px',
              height: '5px',
            }}
            onChange={onChangePassword}
          ></input>
          <input
            className="na"
            placeholder="닉네임"
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
              top: '-11px',
              height: '5px',
            }}
            onChange={onChangePassword}
          ></input>
          <input
            className="ta"
            placeholder="키"
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
              top: '-6px',
              height: '5px',
            }}
            onChange={onChangePassword}
          ></input>
          <input
            className="wg"
            placeholder="몸무게"
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
              top: '1px',
              height: '5px',
            }}
            onChange={onChangePassword}
          ></input>
          <SigninButton onClick={SignUpUser}>회원가입</SigninButton>
        </Contents>
      </ModalWrap>
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
  text-align: center;
`;

export default Signup;

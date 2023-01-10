import styled from 'styled-components';
import { CloseOutlined } from '@ant-design/icons';
import { useState } from 'react';

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

  const SignIn = () => {
    console.log(id);
    console.log(password);
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
          <h1>회원가입</h1>
          <input className="id" onChange={onChangeId}></input>
          <input className="pw" onChange={onChangePassword}></input>
          <input className="pw" onChange={onChangePassword}></input>
          <input className="pw" onChange={onChangePassword}></input>
          <input className="pw" onChange={onChangePassword}></input>
          <SigninButton onClick={SignIn}>회원가입</SigninButton>
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
`;

export default Signup;

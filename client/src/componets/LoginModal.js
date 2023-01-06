import styled from 'styled-components';
const LoginModal = () => {
  return (
    <>
      <div>
        <SModalBack></SModalBack>
        <SModal>
          <SModalView></SModalView>
        </SModal>
      </div>
    </>
  );
};

const SModalBack = styled.div`
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.4);
  position: fixed;
  top: 0;
  overflow: hidden;
`;
export const SModal = styled.div`
  position: fixed;
  top: 0;
  overflow: hidden;
  top: 15%;
  left: 39%;
`;
export const SModalView = styled.div`
  width: 350px;
  height: 600px;
  background-color: #f1f2f3;
  display: flex;
  justify-content: center;
  flex-direction: column;
  align-items: center;
  > button {
    margin-left: 320px;
    margin-top: -50px;
    font-size: 25px;
    background-color: none;
    border: none;
  }
`;

export default LoginModal;

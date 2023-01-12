import { useRef, useState } from 'react';
import styled from 'styled-components';

const ImageInput = () => {
  const [imgFile, setImgFile] = useState([]); // 이미지 배열
  const upload = useRef();

  const imgUpload = () => {
    console.log(upload.current.files);
    setImgFile((prev) => [
      ...prev,
      URL.createObjectURL(upload.current.files[0]),
    ]);
  };

  return (
    <SWrapper>
      <input
        type="file"
        ref={upload} //참조
        onChange={imgUpload} // 파일이 추가되면 이벤트가 일어난다.
        multiple // 파일 여러개 선택 가능
        accept="image/*" //모든 이미지 파일형식
      />

      <SImagefiles>
        <div style={{ display: 'flex' }}>
          {/* input에 파일을 넣어줄때마다 state로 값을 배열로 저장해서, 배열의 길이만큼 이미지를 생성 */}
          {imgFile?.map((img, idx) => (
            <div key={idx} style={{ margin: '20px' }}>
              <img src={img} alt="img" />
            </div>
          ))}
        </div>
      </SImagefiles>
    </SWrapper>
  );
};

const SWrapper = styled.div`
  input {
    z-index: 2;
    margin-bottom: 12px;
    cursor: pointer;
  }
`;
const SImagefiles = styled.div`
  img {
    width: 180px;
    height: 200px;
  }
`;
export default ImageInput;

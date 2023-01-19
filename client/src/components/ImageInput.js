import styled from 'styled-components';
import PropTypes from 'prop-types';

//Postupload.js에서 props로 받아온 것
const ImageInput = ({ imgFile, setImgFile }) => {
  // const [imgFile, setImgFile] = useState([]); // 이미지 배열

  // 이미지 상대경로 저장
  const onChangeImg = (e) => {
    const imageLists = e.target.files;
    let imageUrlLists = [...imgFile];

    for (let i = 0; i < imageLists.length; i++) {
      //미리보기 기능은 위의 결과값에서 없는 상대경로가 필요- URL.createObjectUrl() 메소드
      const currentImageUrl = URL.createObjectURL(imageLists[i]);
      imageUrlLists.push(currentImageUrl);
    }
    //이미지첨부 1장 제한
    if (imageUrlLists.length > 1) {
      imageUrlLists = imageUrlLists.slice(0, 1);
    }
    setImgFile(imageUrlLists);
  };

  return (
    <SWrapper>
      <div className="image-upload ">
        <label htmlFor="input-file">
          <div className="btn-upload">업로드</div>
        </label>
        <input
          type="file"
          id="input-file"
          onChange={onChangeImg} // 파일이 추가되면 이벤트 발생
          multiple // 파일 여러개 선택 가능
          accept="image/*" //모든 이미지 파일형식
        />
      </div>

      <SImagefiles>
        <div className="imageadd">
          {/* input에 파일을 넣어줄때마다 state로 값을 배열로 저장해서, 배열의 길이만큼 이미지를 생성 */}
          {imgFile?.map((img, idx) => (
            <div key={idx}>
              <img src={img} alt="img" />
            </div>
          ))}
        </div>
      </SImagefiles>
    </SWrapper>
  );
};

ImageInput.propTypes = {
  imgFile: PropTypes.array,
  setImgFile: PropTypes.func,
};

const SWrapper = styled.div`
  width: 15vw;
  .image-upload {
  }
  .image-upload > input {
    display: none;
  }
  //파일 업로드버튼
  .btn-upload {
    width: 3.5vw;
    height: 3vh;
    /* padding: 7px 14px; */
    background-color: #d9d4a6;
    border-radius: 4px;
    color: white;
    font-size: 13px;
    display: flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;
  }
`;
const SImagefiles = styled.div`
  .imageadd {
    display: flex;
  }
  img {
    width: 12vw;
    height: 26vh;
    margin: 20px 3px;
  }
`;
export default ImageInput;
